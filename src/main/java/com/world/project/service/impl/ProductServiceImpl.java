package com.world.project.service.impl;

import com.world.project.entity.Error;
import com.world.project.entity.Product;
import com.world.project.entity.ProductResponse;
import com.world.project.repository.ProductRepository;
import com.world.project.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductResponse getProductById(String productId) {
        ProductResponse productResponse = new ProductResponse();
        Error error = productIdValidation(productId);
        log.info("Error: " + error);
        if(null != error){
            productResponse.setError(error);
            return productResponse;
        }

        Optional<Product> product = productRepository.findByProductId(productId);

        if (product.isPresent()) {
            productResponse.setProduct(product.get());
        } else {
            productResponse.setError(new Error(HttpStatus.NOT_FOUND.value(), "Product not found with id : " + productId));
        }
        return productResponse;
    }

    @Override
    public ProductResponse createProduct(Product product) {
        ProductResponse productResponse = new ProductResponse();
        Error error = productValidation(product);
        if(null != error){
            productResponse.setError(error);
            return productResponse;
        }

        log.info("Came here 1");
        Optional<Product> isAlreadyPresent = productRepository.findByProductId(product.getProductId());

        if(isAlreadyPresent.isPresent()){
            productResponse.setError(new Error(HttpStatus.CONFLICT.value(), "A Product with product id - " +
                    product.getProductId() + " already present"));
            return productResponse;
        }
        product.setCreatedAt(LocalDate.now());
        Product savedProduct = productRepository.save(product);
        productResponse.setProduct(savedProduct);

        return productResponse;
    }

    @Override
    public ProductResponse updateProduct(String productId, Product product) {
        ProductResponse productResponse = new ProductResponse();
        Error error = productIdValidation(productId);
        if(null != error){
            productResponse.setError(error);
            return productResponse;
        }

        Optional<Product> optionalProduct = productRepository.findByProductId(productId);

        if (optionalProduct.isPresent()) {
            Product updateProduct = optionalProduct.get();

            if(null != product.getName()) updateProduct.setName(product.getName());
            if(null != product.getPrice()) updateProduct.setPrice(product.getPrice());
            if(null != product.getDescription()) updateProduct.setDescription(product.getDescription());

            updateProduct.setUpdatedAt(LocalDate.now());

            updateProduct = productRepository.save(updateProduct);
            productResponse.setProduct(updateProduct);
            return productResponse;
        } else {
            productResponse.setError(new Error(HttpStatus.NOT_FOUND.value(), "Product not found with id : " + product.getId()));
        }
        return productResponse;
    }

    @Override
    public ProductResponse deleteProduct(String productId) {
        ProductResponse productResponse = new ProductResponse();
        Error error = productIdValidation(productId);
        if(null != error){
            productResponse.setError(error);
            return productResponse;
        }
        Optional<Product> deleteProduct = productRepository.findByProductId(productId);

        if (deleteProduct.isPresent()) {
            productRepository.delete(deleteProduct.get());
            productResponse.setProduct(deleteProduct.get());
        } else {
            productResponse.setError(new Error(HttpStatus.NOT_FOUND.value(), "Product not found with id : " + productId));
        }

        return productResponse;
    }


    public Error productIdValidation(String productId) {
        Error error = new Error();
        if (StringUtils.isBlank(productId)) {
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Product Id not provided");
            return error;
        }

        if (productId.matches("[^a-zA-Z0-9]")) {
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Product Id contains special characters : " + productId);
            return error;
        }

        return null;
    }

    public Error productValidation(Product product) {
        Error error = new Error();
        if (StringUtils.isBlank(product.getProductId())) {
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Product Id not provided");
            return error;
        }

        if (StringUtils.isBlank(product.getName())) {
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Product Name not provided");
            return error;
        }

        if (StringUtils.isBlank(product.getPrice())) {
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Product Price not provided");
            return error;
        }

        if (product.getProductId().matches("[^a-zA-Z0-9]")) {
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Product Id contains special characters : " + product.getProductId());
            return error;
        }

        if (!isFloat(product.getPrice())) {
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Price contains alphabetical characters : " + product.getPrice());
            return error;
        }

        return null;
    }

    public static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}