package com.sparta.msa_exam.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponseDto implements Serializable{

    private Long order_id;
    private List<Long> product_ids;

    public OrderResponseDto(Long order_id, List<Long> product_ids) {
        this.order_id = order_id;
        this.product_ids = product_ids;
    }
}
