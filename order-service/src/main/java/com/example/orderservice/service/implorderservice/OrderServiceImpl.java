package com.example.orderservice.service.implorderservice;

import com.example.orderservice.config.WebClientConfig;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.ProductOrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.ProductOrder;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    WebClientConfig webClientConfig;
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setNumberOrder(UUID.randomUUID().toString());
        List<ProductOrder> productOrder = orderRequest.getProductOrderList()
                .stream()
                .map(productOrderRequest -> mapToProductOrder(productOrderRequest))
                .toList();
        order.setProductOrderList(productOrder);
        orderRepository.save(order);
    }

    private ProductOrder mapToProductOrder(ProductOrderRequest productOrderRequest) {
        return ProductOrder.builder()
                .idProduct(productOrderRequest.getIdProduct())
                .quantity(productOrderRequest.getQuantity())
                .price(productOrderRequest.getPrice())
                .build();
    }
}
