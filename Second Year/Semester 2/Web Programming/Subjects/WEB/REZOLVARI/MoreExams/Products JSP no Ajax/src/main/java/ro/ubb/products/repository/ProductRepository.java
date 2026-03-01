package ro.ubb.products.repository;

import ro.ubb.products.model.Product;

public interface ProductRepository  extends MainRepository<Product, Long>{
    Product findByName(String name);
}
