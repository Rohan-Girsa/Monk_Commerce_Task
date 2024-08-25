package com.monk.response;

import com.monk.entity.Coupon;
import com.monk.model.Cart;
import com.monk.model.StatusDescription;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseWrapper {
    private StatusDescription statusDescription;
    private Coupon coupon;
    private List<Coupon> couponList;
    private Cart cart;
}
