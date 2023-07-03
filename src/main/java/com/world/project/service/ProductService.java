package com.world.project.service;

import com.world.project.entity.Product;
import com.world.project.entity.ProductResponse;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    ProductResponse getProductById(String productId);
    ProductResponse createProduct(Product product);
    ProductResponse updateProduct(String productId,Product product);
    ProductResponse deleteProduct(String productId);
}
