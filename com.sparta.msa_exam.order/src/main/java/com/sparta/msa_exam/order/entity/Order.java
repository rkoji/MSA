package com.sparta.msa_exam.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    private String name;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> products_ids = new ArrayList<>();

    public Order(String name, List<OrderItem> orderItems) {
        this.name = name;
        setOrderItems(orderItems);
    }

    public Order(String name) {
        this.name = name;
    }

    public void addOrderItem(OrderItem orderItem) {
        products_ids.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.products_ids.clear();
        if (orderItems != null) {
            orderItems.forEach(this::addOrderItem);
        }
    }
}
