package com.example.productservice.service;

import com.example.productservice.dto.CheckIsInStock;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.ProductUpdate;
import com.example.productservice.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProduct();
    List<CheckIsInStock> isInStock(List<String> listIdProduct);
    ProductResponse updateProduct(ProductUpdate productUpdate, String id);
    void deleteProduct(String id);
}
