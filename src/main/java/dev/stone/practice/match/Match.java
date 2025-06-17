package dev.stone.practice.match;

import dev.stone.practice.config.Config;
import dev.stone.practice.match.task.MatchRespawnTask;
import dev.stone.practice.profile.cooldown.CooldownType;
import dev.stone.practice.util.*;
import lombok.Getter;
import lombok.Setter;
import dev.stone.practice.Phantom;
import dev.stone.practice.arena.Arena;
import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.events.MatchEndEvent;
import dev.stone.practice.events.MatchStartEvent;
import dev.stone.practice.events.MatchStateChangeEvent;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.entity.MatchEntity;
import dev.stone.practice.match.task.MatchNewRoundTask;
import dev.stone.practice.match.task.MatchResetTask;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamColor;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.queue.QueueType;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 08/06/2025
 * Project: PotPvpReprised
 */

@Getter
public abstract class Match {

    protected final Phantom plugin = Phantom.getInstance();


    @Getter private static final Map<UUID, PostMatchInventory> postMatchInventories = new HashMap<>();

    private final UUID uuid = UUID.randomUUID();
    @Setter private boolean duel = true;
    @Setter private QueueType queueType = QueueType.UNRANKED;
    protected final Arena arenaDetail;
    private final Kit kit;
    private final List<Team> teams;
    private MatchState state = MatchState.STARTING;
    private final List<UUID> spectators = new ArrayList<>();
    private final List<MatchEntity> entities = new ArrayList<>();
    private final List<Location> placedBlocks = new ArrayList<>();
    private final List<TaskTicker> tasks = new ArrayList<>();

    private final long startTimestamp = System.currentTimeMillis();

    public Match(Arena arenaDetail, Kit kit, List<Team> teams) {
        this.arenaDetail = arenaDetail;
        this.kit = kit;
        this.teams = teams;

        Phantom.getInstance().getMatchHandler().getMatches().put(uuid, this);
    }

    /**
     * Start the match
     */
    public void start() {
        setupTeamSpawnLocation();

        //Arena setup logic

        //Check if the kit allows block building and breaking. If yes, we set the ArenaDetail to using to prevent player using the same arena
        if (kit.getGameRules().isBuild() || kit.getGameRules().isSpleef()) {
            if (Phantom.getInstance().getMatchHandler().getMatches().values().stream().filter(match -> match != this).anyMatch(match -> (match.getKit().getGameRules().isBuild() || match.getKit().getGameRules().isSpleef()) && match.getArenaDetail() == arenaDetail)) {
                end(true, "Another battle is using this field, and the battle's profession requires the use of blocks");
                return;
            }
           // arenaDetail.setUsing(true);
        }

        if(kit.getGameRules().isBuild()) {
            arenaDetail.takeSnapshot();
        }



        for (Player player : getMatchPlayers()) {
            PlayerProfile profile = PlayerProfile.getByUuid(player.getUniqueId());
            profile.setMatch(this);
            profile.setState(ProfileState.FIGHTING);

            PlayerUtil.reset(player);


            player.addPotionEffects(kit.getEffects());
            player.setMaximumNoDamageTicks(kit.getDamageTicks());

            if (kit.getGameRules().isReceiveKitLoadoutBook()) {
                for (ItemStack itemStack : profile.getKitData().get(kit.getName()).getKitItems(kit)) {
                    if (itemStack.getType().equals(Material.BOOK)) {
                        player.getInventory().setItem(8, itemStack);
                    } else {
                        player.getInventory().addItem(itemStack);
                    }
                }
            } else {
              if(kit.getKitLoadout() != null) {
                  kit.getKitLoadout().apply(this, player);
              }
            }
            player.updateInventory();


            //Set up the knockback

        }

        //Teleport players into their team spawn
        teams.forEach(team -> team.teleport(team.getSpawnLocation()));

        //Set team's color
        if (getMatchType() != MatchType.FFA) {
            for (int i = 0; i < teams.size(); i++) {
                Team team = teams.get(i);
                team.setTeamColor(TeamColor.values()[i]);
                team.getTeamPlayers().forEach(team::dye);
            }
        }

        MatchStartEvent event = new MatchStartEvent(this);
        event.call();

        new MatchNewRoundTask(this, null, false);
    }

    public void end() {
        end(false, null);
    }

    public void end(boolean forced, String reason) {
        if (state == MatchState.ENDING) {
            return;
        }

        Common.debug("Ending " + getClass().getSimpleName() + " fighting (" + teams.stream().map(team -> team.getLeader().getUsername()).collect(Collectors.joining(" vs ")) + ") (Kit: " + kit.getName() + ") (Map: " + arenaDetail.getSchematic().toString() + ") (UUID: " + uuid + ")");
        state = MatchState.ENDING;

        MatchEndEvent event = new MatchEndEvent(this, forced);
        event.call();

        if (forced) {
            broadcastMessage(Lenguaje.MATCH_MESSAGES.END_FORCE.replace("<reason>", reason));
        } else {
            //Setup Post-Match Inventories
            for (TeamPlayer teamPlayer : getWinningPlayers()) {
                PostMatchInventory postMatchInventory = new PostMatchInventory(teamPlayer);
                postMatchInventories.put(teamPlayer.getUuid(), postMatchInventory);
            }
            displayMatchEndTitle();
            displayMatchEndMessages();
            calculateMatchStats();
        }


        //#442 - Teleport back to spawn location to prevent stuck in the portal
        if (kit.getGameRules().isPortalGoal()) {
            getTeams().forEach(t -> t.teleport(t.getSpawnLocation()));
        }

        new MatchResetTask(this);
    }

    /**
     * @param profile A random profile from match players which is alive. This is used to create a score cooldown
     * @param scorer The TeamPlayer who scored the point
     */
    public void score(PlayerProfile profile, TeamPlayer entity, TeamPlayer scorer) {
        Team team = getTeam(scorer);
        team.handlePoint();
        if (state == MatchState.FIGHTING && team.getPoints() < kit.getGameRules().getMaximumPoints()) {
            if (kit.getGameRules().isOnlyLoserResetPositionWhenGetPoint()) {
                new MatchRespawnTask(this, entity);
                return;
            }
          //  new MatchFireworkTask(team.getTeamColor().getDyeColor().getColor(), this);
            new MatchNewRoundTask(this, scorer, true);
            return;
        }

        getOpponentTeam(team).getAliveTeamPlayers().forEach(teamTarget -> die(teamTarget.getPlayer(), false));
    }

    public void die(Player deadPlayer, boolean disconnected) {
        TeamPlayer teamPlayer = getTeamPlayer(deadPlayer);
        PlayerProfile profile = PlayerProfile.get(deadPlayer);
        Team team = getTeam(deadPlayer);

        teamPlayer.setDisconnected(disconnected); //Set the disconnect state here, so player who already die, do /giveup, and do /spec to join back the match will not have duplicate messages

        if (!teamPlayer.isAlive()) {
            return;
        }

        teamPlayer.setAlive(false);
        getMatchPlayers().forEach(VisibilityController::updateVisibility);

        //Setup Post-Match Inventory
        PostMatchInventory postMatchInventory = new PostMatchInventory(teamPlayer);
        postMatchInventories.put(teamPlayer.getUuid(), postMatchInventory);

       // displayDeathMessage(teamPlayer, deadPlayer);

        //Check if there's only one team survives. If yes, end the match
        if (canEnd()) {
            end();
        } else if (!disconnected) {
            PlayerUtil.spectator(deadPlayer);
           // Tasks.runLater(profile::setupItems, 1L);
        }
    }

    public void respawn(TeamPlayer teamPlayer) {
        Player player = teamPlayer.getPlayer();
        Team team = getTeam(player);
        team.getSpawnLocation().clone().add(0,0,0).getBlock().setType(Material.AIR);
        team.getSpawnLocation().clone().add(0,1,0).getBlock().setType(Material.AIR);
        player.teleport(team.getSpawnLocation());
        player.setAllowFlight(false);
        player.setFlying(false);
        teamPlayer.setRespawning(false);
        getMatchPlayers().forEach(VisibilityController::updateVisibility);

       PlayerProfile profile = PlayerProfile.getByUuid(player.getUniqueId());
        //So arrow will not be duplicated if GiveBackArrow is on
        profile.getCooldowns().get(CooldownType.ARROW).cancelCountdown();

        player.setExp(0);
        player.setLevel(0);

        teamPlayer.setProtectionUntil(System.currentTimeMillis() + (3 * 1000));
        teamPlayer.respawn(this);
       // Language.MATCH_RESPAWN_MESSAGE.sendMessage(player);
    }

    public void joinSpectate(Player player, Player target) {
        PlayerProfile profile = PlayerProfile.getByUuid(player.getUniqueId());

        spectators.add(player.getUniqueId());

        getPlayersAndSpectators().forEach(other -> {
            //We do not want to send useless stuff to NPC. 'other' might be null because the NPC might be already destroyed because it is dead
            if (other != null) {
                PlayerProfile otherProfile = PlayerProfile.getByUuid(other.getUniqueId());
            }
        });

        profile.setMatch(this);
        profile.setState(ProfileState.SPECTATING);
        PlayerUtil.spectator(player);
       // profile.setupItems();

        player.teleport(arenaDetail.getSpectatorSpawn());

    }

    public void leaveSpectate(Player player) {
        spectators.remove(player.getUniqueId());

        getPlayersAndSpectators().forEach(other -> {
            //We do not want to send useless stuff to NPC. 'other' might be null because the NPC might be already destroyed because it is dead
            if (other != null) {
                PlayerProfile otherProfile = PlayerProfile.getByUuid(other.getUniqueId());
            }
        });

        plugin.getLobbyManager().sendToSpawnAndReset(player);
    }

    public void addDroppedItem(Item item, String whoDropped) {
        getEntities().add(new MatchEntity(this, item));
    }

    public void clearEntities(boolean forced) {
        Iterator<MatchEntity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            MatchEntity matchEntity = iterator.next();
            if (forced || System.currentTimeMillis() - matchEntity.getTimestamp() >= 10000) {
                matchEntity.getEntity().remove();
                iterator.remove();
            }
        }
    }

    public void setState(MatchState state) {
        this.state = state;

       getPlayersAndSpectators().forEach(VisibilityController::updateVisibility);

        MatchStateChangeEvent event = new MatchStateChangeEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    public boolean canEnd() {
        return teams.stream().filter(team -> !team.isEliminated()).count() <= 1;
    }

    public String getRelationColor(Player viewer, Player target) {
        if (viewer.equals(target)) {
            return CC.GREEN;
        }

        Team team = getTeam(target);
        Team viewerTeam = getTeam(viewer);

        if (team == null || viewerTeam == null) {
            return Lenguaje.MATCH_MESSAGES.NAMETAG_OTHER;
        }

        if (team.equals(viewerTeam)) {
            return Lenguaje.MATCH_MESSAGES.NAMETAG_TEAMMATE;
        } else {
            return Lenguaje.MATCH_MESSAGES.NAMETAG_OPPONENT;
        }
    }



    /**
     * @return A list of players who are in the match, without spectators and disconnected players
     */
    public List<Player> getMatchPlayers() {
        List<Player> players = new ArrayList<>();
        teams.forEach(team -> players.addAll(team.getTeamPlayers().stream()
                //Filter all players who are already disconnected
                .filter(tP -> !tP.isDisconnected())
                //Convert all TeamPlayer to Player
                .map(TeamPlayer::getPlayer)
                //TeamPlayer#isDisconnected will be false if the player is already dead, and disconnected afterwards. This is why we have to filter nonNull objects
                .filter(Objects::nonNull)
                .collect(Collectors.toList())));
        return players;

    }


    public List<Player> getSpectators() {
        List<Player> players = new ArrayList<>();
        spectators.forEach(spectatorUUID -> players.add(Bukkit.getPlayer(spectatorUUID)));
        return players;
    }

    public List<Player> getPlayersAndSpectators() {
        List<Player> players = new ArrayList<>(getMatchPlayers());
        players.addAll(getSpectators());
        return players;
    }

    public Team getTeam(TeamPlayer player) {
        for (Team team : teams) {
            if (team.getTeamPlayers().stream().filter(Objects::nonNull).collect(Collectors.toList()).contains(player)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeam(Player player) {
        for (Team team : teams) {
            if (team.getTeamPlayers().stream().map(TeamPlayer::getPlayer).filter(Objects::nonNull).collect(Collectors.toList()).contains(player)) {
                return team;
            }
        }
        return null;
    }

    public TeamPlayer getTeamPlayer(Player player) {
        return getTeamPlayer(player.getUniqueId());
    }

    public TeamPlayer getTeamPlayer(UUID uuid) {
        for (Team team : teams) {
            for (TeamPlayer teamPlayer : team.getTeamPlayers()) {
                if (teamPlayer.getUuid().equals(uuid)) {
                    return teamPlayer;
                }
            }
        }
        return null;
    }

    public boolean isProtected(Location location, boolean isPlacing) {
        return isProtected(location, isPlacing, null);
    }

    public boolean isProtected(Location location, boolean isPlacing, Block block) {
        if (block != null && block.getType() == Material.TNT) { //Allow TNT placing above build limit
            return false;
        }
        if (location.getBlockY() >= arenaDetail.getMaxbuild() || location.getBlockY() <= arenaDetail.getDeadzone()) {
            return true;
        }
        if (!arenaDetail.getBounds().contains(location)) {
            return true;
        }
        if (kit.getGameRules().isSpleef()) {
            return location.getBlock().getType() != Material.SNOW_BLOCK && location.getBlock().getType() != Material.SAND;
        }
        if (kit.getGameRules().isBed()) {
            switch (location.getBlock().getType()) {
                case BED_BLOCK:
                case WOOD:
                case ENDER_STONE:
                    return false;
            }
        }
        if (kit.getGameRules().isBreakGoal() && location.getBlock().getType() == Material.BED_BLOCK) {
            return false;
        }
        if (kit.getGameRules().isPortalGoal()) {
            long count = Util.getBlocksAroundCenter(location, arenaDetail.getPortalProtecion()).stream().filter(b -> b.getType() == Material.ENDER_PORTAL).count();
            if (count > 0) {
                return true;
            }
            if (location.getBlock().getType() == Material.STAINED_CLAY && (location.getBlock().getData() == 0 || location.getBlock().getData() == 11 || location.getBlock().getData() == 14)) {
                return false;
            }
        }
        if (Config.MATCH_ALLOW_BREAK_BLOCKS.contains(location.getBlock().getType().name())) {
            return false;
        }
        if (!isPlacing) {
            return !getPlacedBlocks().contains(location);
        }
        return false;
    }

    public List<TeamPlayer> getTeamPlayers() {
        List<TeamPlayer> players = new ArrayList<>();
        teams.stream().map(Team::getTeamPlayers).forEach(players::addAll);
        return players.stream().filter(teamPlayer -> !teamPlayer.isDisconnected()).collect(Collectors.toList());
    }

    public int getMaximumBoxingHits() {
        if (!kit.getGameRules().isBoxing()) {
         Common.debug("Feature No support");
        }
        Team team = getTeams().stream().max(Comparator.comparing(t -> t.getTeamPlayers().size())).orElse(null);
        if (team == null) {

        }
        return team.getTeamPlayers().size() * 100;
    }

    public long getElapsedDuration() {
        return System.currentTimeMillis() - startTimestamp;
    }

    public void broadcastMessage(String... message) {
        getPlayersAndSpectators().forEach(player -> Common.sendMessage(player, message));
    }
    public void broadcastMessage(List<String> messages) {
        getPlayersAndSpectators().forEach(player -> Common.sendMessage(player, messages));
    }
    public void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        getPlayersAndSpectators().forEach(player -> {
            TitleSender.sendTitle(player, title, PacketPlayOutTitle.EnumTitleAction.TITLE, fadeIn, stay, fadeOut);
            TitleSender.sendTitle(player, subtitle, PacketPlayOutTitle.EnumTitleAction.SUBTITLE, fadeIn, stay, fadeOut);
        });
    }
    public void broadcastTitle(String message) {
        getPlayersAndSpectators().forEach(player -> TitleSender.sendTitle(player, message, PacketPlayOutTitle.EnumTitleAction.TITLE, 0, 21, 5));
    }
    public void broadcastSubTitle(String message) {
        getPlayersAndSpectators().forEach(player -> TitleSender.sendTitle(player, message, PacketPlayOutTitle.EnumTitleAction.SUBTITLE, 0, 21, 5));
    }
    public void broadcastTitle(Team team, String message) {
        for (TeamPlayer teamPlayer : team.getTeamPlayers())
            if (teamPlayer.getPlayer() != null)
                TitleSender.sendTitle(teamPlayer.getPlayer(), message, PacketPlayOutTitle.EnumTitleAction.TITLE, 0, 21, 5);
    }
    public void broadcastSubTitle(Team team, String message) {
        for (TeamPlayer teamPlayer : team.getTeamPlayers())
            if (teamPlayer.getPlayer() != null)
                TitleSender.sendTitle(teamPlayer.getPlayer(), message, PacketPlayOutTitle.EnumTitleAction.SUBTITLE, 0, 21, 5);
    }
    public void broadcastSound(Sound sound) {
        getPlayersAndSpectators().forEach(player -> player.playSound(player.getLocation(), sound, 10, 1));
    }

    public void broadcastSound(Team team, Sound sound) {
        team.getTeamPlayers().stream().map(TeamPlayer::getPlayer).filter(Objects::nonNull).forEach(player -> player.playSound(player.getLocation(), sound, 10, 1));
    }

    public void broadcastSpectatorsSound(Sound sound) {
        getSpectators().forEach(player -> player.playSound(player.getLocation(), sound, 10, 1));
    }
    public void logPlayersInMatch() {
        // only for testing
        List<Player> players = getMatchPlayers();
        for (Player player : players) {
            Bukkit.getLogger().info("Jugador en el match: " + player.getName());
        }
    }



    public void SoundDeath(TeamPlayer teamPlayer, Player death) {

        Player team = teamPlayer.getPlayer();
        team.playSound(team.getLocation(), Sound.GLASS, 1.0f, 2.0f);
        death.playSound(death.getLocation(), Sound.GLASS, 1.0f, 2.0f);

    }

    public abstract void setupTeamSpawnLocation();

    public abstract void displayMatchEndMessages();

    public abstract void displayMatchEndTitle();

    public abstract void calculateMatchStats();

    public abstract MatchType getMatchType();

    public abstract Team getOpponentTeam(Team team);

    public abstract TeamPlayer getOpponent(TeamPlayer teamPlayer);
    public abstract List<TeamPlayer> getWinningPlayers();

    public abstract Team getWinningTeam();

    public abstract List<String> getMatchScoreboard(Player player);

    public abstract List<String> getSpectateScoreboard(Player player);
}
