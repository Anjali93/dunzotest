import exceptions.BeverageNotFoundException;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

@AllArgsConstructor
@ToString
public class Machine {

    private int outlets;
    private Map<String, Integer> machineItems = new HashMap<String, Integer>();
    private Map<String, Beverage> beverages = new HashMap<String, Beverage>();
    private Integer lowCapacity;

    private static Semaphore semaphore;

    public boolean serve(String beverageName) throws BeverageNotFoundException, InterruptedException {

        try {
            synchronized (this){
                if(semaphore == null){
                    semaphore = new Semaphore(outlets);
                }
            }
            semaphore.acquire();
            if (!beverages.containsKey(beverageName)) {
                throw new BeverageNotFoundException("Beverage not found");
            }

            Beverage beverage = beverages.get(beverageName);
            synchronized (this) {
                if (check(beverage)) {
                    reduce(beverage);
                    System.out.println(beverage.getName() + " Is served");
                    return true;
                } else {
                    return false;
                }
            }
        } finally {
            semaphore.release();
        }
    }

    private boolean check(Beverage beverage){

        for (Map.Entry<String,Integer> entry : beverage.getIngredients().entrySet()) {

            if (!machineItems.containsKey(entry.getKey())){
                System.out.println(beverage.getName() + " Cannot be served as " + entry.getKey() + " is not available");
                return false;
            }
            else if (machineItems.containsKey(entry.getKey()) && machineItems.get(entry.getKey()) - entry.getValue() < 0) {
                System.out.println(beverage.getName() + " Cannot be served as " + entry.getKey() + " is not sufficient");
                return false;
            }
        }
        return true;
    }

    private void reduce(Beverage beverage){

        for (Map.Entry<String,Integer> entry : beverage.getIngredients().entrySet()) {
            int updatedAmount = machineItems.get(entry.getKey()) - entry.getValue();
            machineItems.put(entry.getKey(), updatedAmount);
        }
    }

    public void refill(String ingredient, Integer quantity) {

        if (!machineItems.containsKey(ingredient)){
            machineItems.put(ingredient, 0);
        }
        machineItems.put(ingredient, machineItems.get(ingredient) + quantity);
    }

    public void printLowItems(){
        machineItems.entrySet().forEach(e -> {
            if(e.getValue() < lowCapacity){
                System.out.println("Low item : " + e.getKey() + " : " + e.getValue());
            }
        });
    }
}
