package com.world.project.controllers;

import com.world.project.entity.Product;
import com.world.project.entity.ProductResponse;
import com.world.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> product = productService.getAllProducts();
        return ResponseEntity
                .ok()
                .body(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity <ProductResponse> getProductById(@PathVariable String productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        if(productResponse.getProduct() == null){
            return ResponseEntity
                    .status(productResponse.getError().getCode())
                    .body(productResponse);
        }
        return ResponseEntity
                .ok()
                .body(productResponse);
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody Product product) {
        ProductResponse productResponse = productService.createProduct(product);
        if(productResponse.getProduct() == null){
            return ResponseEntity
                    .status(productResponse.getError().getCode())
                    .body(productResponse);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productResponse);
    }

    @PutMapping("/{productId}")
    public ResponseEntity <ProductResponse> updateProduct(@PathVariable String productId, @RequestBody Product product) {
        product.setProductId(productId);
        ProductResponse productResponse = productService.updateProduct(productId, product);
        if(productResponse.getProduct() == null){
            return ResponseEntity
                    .status(productResponse.getError().getCode())
                    .body(productResponse);
        }
        return ResponseEntity
                .ok()
                .body(productResponse);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity <ProductResponse> deleteProduct(@PathVariable String productId) {
        ProductResponse productResponse = productService.deleteProduct(productId);
        if(productResponse.getProduct() == null){
            return ResponseEntity
                    .status(productResponse.getError().getCode())
                    .body(productResponse);
        }

        return ResponseEntity
                .ok()
                .body(productResponse);
    }
}
