package dev.stone.practice.adapter.board;


import lombok.Setter;
import dev.stone.practice.adapter.board.adapter.ScoreboardAdapter;
import dev.stone.practice.adapter.board.fastboard.FastBoard;
import dev.stone.practice.util.CC;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 26/05/2025
 * Project: Amber
 */
@Setter
public class Board {

    private Player player;
    private FastBoard fastBoard;
    private ScoreboardAdapter adapter;

    public Board(Player player) {
        this.player = player;
        this.fastBoard = new FastBoard(player);
        this.adapter = new ScoreboardAdapter();
    }

    public void update() {
        List<String> lines = adapter.getScoreboard(player);

        // We destroy the board if the lines are null or empty
        if (lines == null || lines.isEmpty()) {
            if (!fastBoard.isDeleted()) fastBoard.delete();
            return;
        }

        // create a new fast-board otherwise updating a deleted one will throw an exception.
        if (fastBoard.isDeleted()) {
            fastBoard = new FastBoard(player);
        }
        fastBoard.setTitle(CC.translate(adapter.getTitle(player)));
        fastBoard.setLines(CC.translate(adapter.getScoreboard(player)));
    }

}
