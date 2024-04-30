package client;

public class Order {
    private final String[] ingredients;
    public static Order orderWithIngredients (String[] ingredients){
        return new Order(ingredients);
    }

    public Order(String[] idIngredients) {
        this.ingredients=idIngredients;
    }
}
