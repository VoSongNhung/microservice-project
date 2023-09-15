package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.ProductUpdate;

import java.util.List;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProduct();
    List<ProductResponse> getAllProductByCode(String code);
    ProductResponse updateProduct(ProductUpdate productUpdate, String id);
    void deleteProduct(String id);
}
