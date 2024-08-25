package com.monk.model;

import lombok.Data;

import java.util.List;

@Data
public class Cart {
    private List<CartItem> cartItems;
}
