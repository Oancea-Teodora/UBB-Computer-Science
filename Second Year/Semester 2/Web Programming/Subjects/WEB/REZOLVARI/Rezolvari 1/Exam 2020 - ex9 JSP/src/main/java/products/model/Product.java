package products.model;

public class Product {

    private int id;
    private String name;
    private String description;

    public Product(int id, String n, String d){
        this.id = id;
        this.description = d;
        this.name = n;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String toString(){
        return "Product{ "
                + " name: " + getName();
    }
}
