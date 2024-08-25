package com.monk.controller;

import com.monk.entity.Coupon;
import com.monk.exception.CustomCouponException;
import com.monk.exception.InvalidInputException;
import com.monk.model.Cart;
import com.monk.response.ResponseWrapper;
import com.monk.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService){
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createCoupon(@RequestBody Coupon coupon) throws InvalidInputException {
        ResponseWrapper response = couponService.createCoupon(coupon);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllCoupons() throws CustomCouponException {
        ResponseWrapper response = couponService.getAllCoupons();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getCouponById(@PathVariable String id) throws CustomCouponException{
        ResponseWrapper response = couponService.getCouponById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> updateCoupon(@PathVariable String id, @RequestBody Coupon coupon) throws CustomCouponException, InvalidInputException{
        ResponseWrapper response = couponService.updateCoupon(id, coupon);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> deleteCoupon(@PathVariable String id) throws CustomCouponException{
        ResponseWrapper response = couponService.deleteCouponById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<ResponseWrapper> getApplicableCoupons(@RequestBody Cart cart) throws CustomCouponException{
        ResponseWrapper response = couponService.getAllApplicableCoupons(cart);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<ResponseWrapper> applyCoupon(@PathVariable String id, @RequestBody Cart cart) throws CustomCouponException{
        ResponseWrapper response = couponService.applyCoupon(cart, id);
        return ResponseEntity.ok(response);
    }
}
