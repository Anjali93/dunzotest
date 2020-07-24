package config.models;

import lombok.Data;

import java.util.Map;

@Data
public class Machine {

    private Outlet outlets;
    private Map<String, Integer> total_items_quantity;
    private Map<String, Map<String, Integer>> beverages;
}
