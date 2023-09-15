package com.example.productservice.service.implservice;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.ProductUpdate;
import com.example.productservice.exceptionhandle.NotFoundException;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImplement implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Override
    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .code(productRequest.getCode())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .number(productRequest.getNumber())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product "+productRequest.getName()+" with code "+product.getCode()+ " has been saved");
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(product -> maptoProductResponse(product)).toList();
    }

    @Override
    public List<ProductResponse> getAllProductByCode(String code) {
        List<Product> productList = productRepository.findProductByCode(code);
        return productList.stream().map(product -> maptoProductResponse(product)).toList();
    }

    @Override
    public ProductResponse updateProduct(ProductUpdate productUpdate, String id) {
        Product findproduct = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product is not found"));
        Product product = Product.builder()
                .id(findproduct.getId())
                .code(findproduct.getCode())
                .name(productUpdate.getName())
                .description(productUpdate.getDescription())
                .number(productUpdate.getNumber())
                .price(productUpdate.getPrice())
                .build();
        productRepository.save(product);
        return maptoProductResponse(product);
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public ProductResponse maptoProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .number(product.getNumber())
                .price(product.getPrice())
                .build();
    }
}
