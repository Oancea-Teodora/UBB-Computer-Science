package products.model;

public class Order {
    private int id;
    private String user;
    private int product;
    private int quantity;

    public Order(int id, String u, int p, int q){
        this.id = id;
        this.user = u;
        this.product = p;
        this.quantity = q;
    }

    public Order(String u, int p, int q){
        this.user = u;
        this.product = p;
        this.quantity = q;
    }

    public String getUser(){
        return this.user;
    }

    public int getId(){
        return this.id;
    }

    public int getProduct(){
        return this.product;
    }

    public int getQuantity(){
        return this.quantity;
    }

    @Override
    public String toString(){
        return "Order{ user: " + this.user
                + "\n ; productId: " + this.product;
    }
}
