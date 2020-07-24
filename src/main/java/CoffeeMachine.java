import config.ConfigParser;
import config.models.MachineConfig;
import exceptions.BeverageNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CoffeeMachine {

    public static void main(String[] args) throws IOException, BeverageNotFoundException, InterruptedException {

        ConfigParser parser = new ConfigParser();

        MachineConfig config = parser.parseMachineConfig(
                parser.getClass().getClassLoader().getResource("config.json").getFile());
        System.out.println(config);

        Machine machine = new Machine(
                config.getMachine().getOutlets().getCount_n(),
                config.getMachine().getTotal_items_quantity(),
                config.getMachine().getBeverages().entrySet().stream().map(e -> {
                    return Beverage.builder()
                            .name(e.getKey())
                            .ingredients(e.getValue())
                            .build();
                }).collect(Collectors.toMap(Beverage::getName, e -> e)),
                300
        );

        machine.serve("hot_tea");
        machine.serve("green_tea");

        machine.refill("hot_water", 400);
        machine.refill("hot_water", 800);
        machine.refill("hot_water", 300);
        machine.refill("green_mixture", 300);

        machine.printLowItems();

        machine.serve("green_tea");

        machine.printLowItems();


    }
}
