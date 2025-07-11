package dev.stone.practice.util.menu.pagination;

import dev.stone.practice.util.menu.Button;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class FilterablePaginatedMenu<T> extends PaginatedMenu {

    private final List<PageFilter<T>> filters;
    @Setter
    private int scrollIndex = 0;

    {
        filters = generateFilters();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(7, new PageFilterButton<>(this));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        return getFilteredButtons(player);
    }

    public abstract Map<Integer, Button> getFilteredButtons(Player player);

    public List<PageFilter<T>> generateFilters() {
        return new ArrayList<>();
    }

}
