package com.example.productservice.service.implservice;

import com.example.productservice.dto.CheckIsInStock;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.ProductUpdate;
import com.example.productservice.exceptionhandle.NotFoundException;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImplement implements ProductService {
    @Autowired
    ProductRepository productRepository;
//    KafkaProducerService kafkaProducerService;
//    KafkaProducerServiceImplement kafkaProducerServiceImplement;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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
//        List<String> codeList = productList.stream()
//                .map(Product::getCode)
//                .collect(Collectors.toList());
////        listKafkaProducerService.sendListToKafka(codeList.toString());
//        kafkaTemplate.send("sendCode", codeList.toString());
        return productList.stream().map(product -> maptoProductResponse(product)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckIsInStock> isInStock(List<String> listIdProduct) {
        return productRepository.findByIdIn(listIdProduct)
                .stream().map(product -> CheckIsInStock.builder()
                            .idProduct(product.getId())
                            .isInStock(product.getNumber()>0)
                            .build()
                ).toList();
//        return productList.stream().map(product -> maptoProductResponse(product)).toList();
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
