package com.monk.model;

import lombok.Data;

@Data
public class CartItem {
    private Integer productId;
    private Integer quantity;
    private Double price;
}


