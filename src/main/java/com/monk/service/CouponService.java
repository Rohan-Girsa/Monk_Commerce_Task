package com.monk.service;


import com.monk.entity.Coupon;
import com.monk.exception.CustomCouponException;
import com.monk.exception.InvalidInputException;
import com.monk.model.Cart;
import com.monk.response.ResponseWrapper;

import java.util.List;

public interface CouponService {
    ResponseWrapper createCoupon(Coupon coupon) throws InvalidInputException;

    ResponseWrapper getAllCoupons() throws CustomCouponException;

    ResponseWrapper getCouponById(String id) throws CustomCouponException;

    ResponseWrapper updateCoupon(String id, Coupon updatedCoupon) throws CustomCouponException, InvalidInputException;

    ResponseWrapper deleteCouponById(String id) throws CustomCouponException;

    ResponseWrapper getAllApplicableCoupons(Cart cart) throws CustomCouponException;

    ResponseWrapper applyCoupon(Cart cart, String id) throws CustomCouponException;
}
