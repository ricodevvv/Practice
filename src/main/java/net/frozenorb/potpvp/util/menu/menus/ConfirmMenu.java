package net.frozenorb.potpvp.util.menu.menus;


import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.menu.Button;
import net.frozenorb.potpvp.util.menu.Menu;
import net.frozenorb.potpvp.util.menu.TypeCallback;
import net.frozenorb.potpvp.util.menu.button.ConfirmationButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConfirmMenu extends Menu {

    private final String title;
    private final TypeCallback<Boolean> response;
    private final boolean closeAfterResponse;
    private final Button centerButtons;

    public ConfirmMenu(TypeCallback<Boolean> response, boolean closeAfter, Button centerButtons) {
        this.title = CC.YELLOW + "Confirm this";
        this.response = response;
        this.closeAfterResponse = closeAfter;
        this.centerButtons = centerButtons;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                buttons.put(getSlot(x, y), new ConfirmationButton(true, response, closeAfterResponse));
                buttons.put(getSlot(8 - x, y), new ConfirmationButton(false, response, closeAfterResponse));
            }
        }

        if (centerButtons != null) {
            buttons.put(getSlot(4, 1), centerButtons);
        }

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return title;
    }

}
