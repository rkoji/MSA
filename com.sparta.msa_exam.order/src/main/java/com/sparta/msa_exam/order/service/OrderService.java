package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.ProductClient;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderItem;
import com.sparta.msa_exam.order.repository.OrderItemRepository;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;

    // 주문 추가 API
    @Transactional
//    @CachePut(cacheNames = "orderCache", key = "#result.id")
    public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
        // product_id가 유효한지 검증
        List<Long> productsIds = orderRequestDto.getProducts_ids();
        for (Long productsId : productsIds) {
            productClient.getProduct(productsId);
        }

        Order order = new Order(orderRequestDto.getName());

        List<OrderItem> orderItems = productsIds.stream()
                .map(productId -> new OrderItem(order, productId))
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return new OrderResponseDto(savedOrder.getOrder_id(), productsIds);
    }

    // 주문에 상품을 추가하는 API
    @Transactional
//    @CachePut(cacheNames = "orderCache",key = "args[0]")
    public void addProductToOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // product_id가 유효한지 검증
        productClient.getProduct(productId);

        OrderItem orderItem = new OrderItem(order, productId);

        order.addOrderItem(orderItem);
    }

    // 주문 단건 조회 API
//    @Cacheable(cacheNames = "orderCache", key = "{args[0].orderId, args[1].products_ids}")
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        List<Long> productList = order.getProducts_ids().stream()
                .map(OrderItem::getProduct_id)
                .collect(Collectors.toList());

        return new OrderResponseDto(order.getOrder_id(), productList);
    }


}
