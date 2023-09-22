package com.example.orderservice.service.implorderservice;

import com.example.orderservice.dto.CheckIsInStock;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.ProductOrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.ProductOrder;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
    @Autowired
    OrderRepository orderRepository;
    WebClient webClient;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final WebClient.Builder webClientBuilder;
    @Autowired
    public OrderServiceImpl(KafkaTemplate<String, String> kafkaTemplate, WebClient.Builder webClientBuilder) {
        this.kafkaTemplate = kafkaTemplate;
        this.webClientBuilder = webClientBuilder;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        try{
            Order order = new Order();order.setNumberOrder(UUID.randomUUID().toString());
            List<ProductOrder> productOrder = orderRequest.getProductOrderList()
                    .stream()
                    .map(productOrderRequest -> mapToProductOrder(productOrderRequest))
                    .toList();
            order.setProductOrderList(productOrder);
            List<String> idProduct = order.getProductOrderList().stream().map(ProductOrder::getIdProduct).toList();
            //call product service, and place order if product is in
            CheckIsInStock[] checkIsInStocks = webClient.get()
                    .uri("http://product-service/api/product/listidproduct",uriBuilder -> uriBuilder.queryParam("idProduct",idProduct).build())
                    .retrieve()
                    .bodyToMono(CheckIsInStock[].class)
                    .block();
            boolean allProductInStock = Arrays.stream(checkIsInStocks)
                    .allMatch(CheckIsInStock::isInStock);
            if(allProductInStock){
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic",order.getNumberOrder().toString());
            }
            else {
                throw new IllegalArgumentException("Product is not in stock");
            }
        }
        catch (Exception e){
            logger.error("Error while placing order" + e);
            throw e;
        }
    }

    private ProductOrder mapToProductOrder(ProductOrderRequest productOrderRequest) {
        return ProductOrder.builder()
                .idProduct(productOrderRequest.getIdProduct())
                .quantity(productOrderRequest.getQuantity())
                .price(productOrderRequest.getPrice())
                .build();
    }
}
