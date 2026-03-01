package products.service;


import products.model.DbManager;
import products.model.Order;
import products.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    // SELECT * FROM `products` where name LIKE "pr%";
    public static List<Order> orders;

    // check if user has other products from previous sessions
    public static void createCart(){
        orders = new ArrayList<>();
    }
    public static void emptyCart(){
        orders.removeAll(orders);
    }

    public static void addToCart(String name, int productId, int quantity){
        Order o = new Order(name, productId, quantity);
        orders.add(o);

        System.out.println("Orders: " + orders);
    }

    public static void finalizeCommand(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Product> products = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            for(Order o : orders) {
                String sql = "INSERT INTO orders(user, productid, quantity) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, o.getUser());
                statement.setInt(2, o.getProduct());
                statement.setInt(3, o.getQuantity());

                statement.execute();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        orders.removeAll(orders);
    }

    public static void cancelOrder(){
        orders.removeAll(orders);
    }

    public static List<Product> getAll(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Product> products = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM products";
            statement = connection.prepareStatement(sql);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();



            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String descr = resultSet.getString("description");

                Product p = new Product(productId, name, descr);
                products.add(p);
            }
            resultSet.close();
            return products;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void addProduct(String name, String description) {

        Connection connection = null;
        PreparedStatement statement = null;
        List<Product> products = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "INSERT INTO products ( name, description) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, description);

            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

        public static List<Product> getProducts(String filter){

        Connection connection = null;
        PreparedStatement statement = null;
        List<Product> products = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM products WHERE name LIKE ?";
            statement = connection.prepareStatement(sql);
            String f = filter + "%";
            statement.setString(1, f);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();



            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String descr = resultSet.getString("description");

                Product p = new Product(productId, name, descr);
                products.add(p);
            }
            resultSet.close();
            return products;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }
}
