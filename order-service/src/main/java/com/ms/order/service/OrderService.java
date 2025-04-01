package com.ms.order.service;

import com.ms.order.dto.InventoryResponse;
import com.ms.order.dto.OrderLineItemsDto;
import com.ms.order.dto.OrderRequest;
import com.ms.order.model.Order;
import com.ms.order.model.OrderLineItems;
import com.ms.order.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).toList();

        order.setOrderLineItems(orderLineItems);

        List<String> skewCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkewCode).toList();

        // Call inventory service and place order if its in stock
        InventoryResponse[] inventoryResponses = webClient.get().uri("http://localhost:8082/api/inventory",
                uriBuilder ->
                        uriBuilder.queryParam(
                                "skewCode", skewCodes
                        ).build()
        ).retrieve().bodyToMono(InventoryResponse[].class).block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepo.save(order);
        } else {
            throw new IllegalArgumentException("Product not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkewCode(orderLineItemsDto.getSkewCode());
        return orderLineItems;
    }
}
