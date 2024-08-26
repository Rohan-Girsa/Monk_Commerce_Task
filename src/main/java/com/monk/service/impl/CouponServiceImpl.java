package com.monk.service.impl;

import com.monk.entity.Coupon;
import com.monk.exception.CustomCouponException;
import com.monk.exception.InvalidInputException;
import com.monk.model.BuyProduct;
import com.monk.model.Cart;
import com.monk.model.GetProduct;
import com.monk.repository.CouponRepository;
import com.monk.response.ResponseWrapper;
import com.monk.service.CouponService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository){
        this.couponRepository = couponRepository;
    }

    @Override
    public ResponseWrapper createCoupon(Coupon coupon) throws InvalidInputException{
        if (isCouponValid(coupon)){
            throw new InvalidInputException("Invalid Input");
        }
        couponRepository.save(coupon);
        return ResponseWrapper.builder().coupon(coupon).build();
    }

    @Override
    public ResponseWrapper getAllCoupons() throws CustomCouponException{
        List<Coupon> list = couponRepository.findAll();
        if(list.isEmpty()){
            throw new CustomCouponException("Coupons not found");
        }
        return ResponseWrapper.builder().couponList(list).build();
    }

    @Override
    public ResponseWrapper getCouponById(String id) throws CustomCouponException{
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new CustomCouponException("Coupon not found"));
        return ResponseWrapper.builder().coupon(coupon).build();
    }

    @Override
    public ResponseWrapper updateCoupon(String id, Coupon updatedCoupon) throws CustomCouponException, InvalidInputException {
        if (isCouponValid(updatedCoupon)){
            throw new InvalidInputException("Invalid Input");
        }
        Coupon coupon = couponRepository.findById(id)
                .map(data -> {
                    data.setType(updatedCoupon.getType());
                    data.setCouponDetails(updatedCoupon.getCouponDetails());
                    return couponRepository.save(data);
                }).orElseThrow(() -> new CustomCouponException("Coupon not found"));
        return ResponseWrapper.builder().coupon(coupon).build();
    }

    @Override
    public ResponseWrapper deleteCouponById(String id) throws CustomCouponException{
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new CustomCouponException("Coupon not found"));
        couponRepository.delete(coupon);
        return ResponseWrapper.builder().coupon(coupon).build();
    }

//    @Override
//    public ResponseWrapper getAllApplicableCoupons(Cart cart) throws CustomCouponException {
//        List<Coupon> list = couponRepository.findAll().stream()
//                .filter(coupon -> isCouponApplicable(cart, coupon))
//                .toList();
//        if (list.isEmpty()) {
//            throw new CustomCouponException("Coupon not found");
//        }
//        return ResponseWrapper.builder().couponList(list).build();
//    }

//    @Override
//    public ResponseWrapper applyCoupon(Cart cart, String id) throws CustomCouponException {
//        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new CustomCouponException("Coupon not found"));
//        if(coupon.getExpirationDate().before(new Date())){
//            throw new CustomCouponException("Coupon expired on "+coupon.getExpirationDate());
//        }
//        if ("cart-wise".equals(coupon.getType())) {
//            double discount = coupon.getCouponDetails().getDiscount();
//            cart.getCartItems().forEach(item -> item.setPrice(item.getPrice() - (item.getPrice() * discount / 100)));
//        }
//        if ("product-wise".equals(coupon.getType())) {
//            applyProductWiseDiscount(cart, coupon);
//        }
//        if ("bxgy".equals(coupon.getType())) {
//            applyBxGyDiscount(cart, coupon);
//        }
//        return ResponseWrapper.builder().cart(cart).build();
//    }

//    private boolean isCouponApplicable(Cart cart, Coupon coupon) {
//        if ("cart-wise".equals(coupon.getType())) {
//            double total = cart.getCartItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
//            return total >= coupon.getCouponDetails().getThreshold();
//        }
//        if ("product-wise".equals(coupon.getType())) {
//            return cart.getCartItems().stream().anyMatch(item -> item.getPrice() * item.getQuantity() >= coupon.getCouponDetails().getDiscount());
//        }
//        if ("bxgy".equals(coupon.getType())) {
//            return isBxGyApplicable(cart, coupon);
//        }
//        return false;
//    }

    private boolean isCouponValid(Coupon coupon) {
        return coupon == null ||
                coupon.getType() == null || coupon.getType().isEmpty() ||
                coupon.getCouponDetails() == null ||
                coupon.getCouponDetails().getThreshold() == null ||
                coupon.getCouponDetails().getDiscount() == null ||
                coupon.getExpirationDate() == null;
    }

//    private boolean isBxGyApplicable(Cart cart, Coupon coupon) {
//        for (BuyProduct buyProduct : coupon.getCouponDetails().getBuyProducts()) {
//            boolean matches = cart.getCartItems().stream()
//                    .anyMatch(item -> item.getProductId().equals(buyProduct.getProductId())
//                            && item.getQuantity() >= buyProduct.getQuantity());
//            if (!matches) {
//                return false;
//            }
//        }
//        return true;
//    }

//    private void applyProductWiseDiscount(Cart cart, Coupon coupon) {
//        cart.getCartItems().forEach(item -> {
//            if (item.getPrice() * item.getQuantity() >= coupon.getCouponDetails().getThreshold()) {
//                item.setPrice(item.getPrice() - (item.getPrice() * coupon.getCouponDetails().getDiscount() / 100));
//            }
//        });
//    }

//    private void applyBxGyDiscount(Cart cart, Coupon coupon) {
//        for (GetProduct getProduct : coupon.getCouponDetails().getGetProducts()) {
//            cart.getCartItems().stream()
//                    .filter(item -> item.getProductId().equals(getProduct.getProductId()))
//                    .findFirst()
//                    .ifPresent(item -> item.setPrice(0.0));
//        }
//    }

    @Override
    public ResponseWrapper getAllApplicableCoupons(Cart cart) throws CustomCouponException {
        List<Coupon> applicableCoupons = couponRepository.findAll().stream()
                .filter(coupon -> isCouponApplicableToCart(cart, coupon))
                .toList();

        if (applicableCoupons.isEmpty()) {
            throw new CustomCouponException("No applicable coupons found");
        }

        return ResponseWrapper.builder().couponList(applicableCoupons).build();
    }

    private boolean isCouponApplicableToCart(Cart cart, Coupon coupon) {
        switch (coupon.getType()) {
            case "cart-wise":
                return isCartWiseCouponApplicable(cart, coupon);
            case "product-wise":
                return isProductWiseCouponApplicable(cart, coupon);
            case "bxgy":
                return isBxGyApplicable(cart, coupon);
            default:
                return false;
        }
    }

    private boolean isCartWiseCouponApplicable(Cart cart, Coupon coupon) {
        double totalCartValue = cart.getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        return totalCartValue >= coupon.getCouponDetails().getThreshold();
    }

    private boolean isProductWiseCouponApplicable(Cart cart, Coupon coupon) {
        return cart.getCartItems().stream()
                .anyMatch(item -> item.getPrice() >= coupon.getCouponDetails().getThreshold());
    }

    private boolean isBxGyApplicable(Cart cart, Coupon coupon) {
        for (BuyProduct buyProduct : coupon.getCouponDetails().getBuyProducts()) {
            boolean matches = cart.getCartItems().stream()
                    .anyMatch(item -> item.getProductId().equals(buyProduct.getProductId())
                            && item.getQuantity() >= buyProduct.getQuantity());
            if (!matches) {
                return false;
            }
        }
        return true;
    }


    @Override
    public ResponseWrapper applyCoupon(Cart cart, String id) throws CustomCouponException {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CustomCouponException("Coupon not found"));

        if (coupon.getExpirationDate().before(new Date())) {
            throw new CustomCouponException("Coupon expired on " + coupon.getExpirationDate());
        }

        switch (coupon.getType()) {
            case "cart-wise":
                applyCartWiseDiscounts(cart, coupon);
                break;
            case "product-wise":
                applyDiscountsToEligibleProducts(cart, coupon);
                break;
            case "bxgy":
                if (isBuyXGetYApplicable(cart, coupon)) {
                    applyBuyXGetYDiscounts(cart, coupon);
                } else {
                    throw new CustomCouponException("Buy X Get Y coupon criteria not met");
                }
                break;
            default:
                throw new CustomCouponException("Invalid coupon type");
        }

        return ResponseWrapper.builder().cart(cart).build();
    }

    private void applyCartWiseDiscounts(Cart cart, Coupon coupon) {
        double totalCartValue = cart.getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        if (totalCartValue >= coupon.getCouponDetails().getThreshold()) {
            double discountRate = coupon.getCouponDetails().getDiscount() / 100.0;
            double discountAmount = totalCartValue * discountRate;
            cart.getCartItems().forEach(item -> {
                double itemTotal = item.getPrice() * item.getQuantity();
                double itemDiscount = itemTotal * discountRate;
                double newItemPrice = item.getPrice() - (itemDiscount / item.getQuantity());
                item.setPrice(newItemPrice);
            });
        }
    }

    private void applyDiscountsToEligibleProducts(Cart cart, Coupon coupon) {
        cart.getCartItems().forEach(item -> {
            if (item.getPrice() >= coupon.getCouponDetails().getThreshold()) {
                double discountRate = coupon.getCouponDetails().getDiscount() / 100.0;
                double newPrice = item.getPrice() - (item.getPrice() * discountRate);
                item.setPrice(newPrice);
            }
        });
    }

    private boolean isBuyXGetYApplicable(Cart cart, Coupon coupon) {
        for (BuyProduct buyProduct : coupon.getCouponDetails().getBuyProducts()) {
            boolean matches = cart.getCartItems().stream()
                    .anyMatch(item -> item.getProductId().equals(buyProduct.getProductId())
                            && item.getQuantity() >= buyProduct.getQuantity());
            if (!matches) {
                return false;
            }
        }
        return true;
    }

    private void applyBuyXGetYDiscounts(Cart cart, Coupon coupon) {
        for (BuyProduct buyProduct : coupon.getCouponDetails().getBuyProducts()) {
            cart.getCartItems().stream()
                    .filter(item -> item.getProductId().equals(buyProduct.getProductId()))
                    .forEach(item -> {
                        int totalQuantity = item.getQuantity();
                        int buyQuantity = buyProduct.getQuantity();
                        int freeItems = totalQuantity / (buyQuantity + 1);

                        if (freeItems > 0) {
                            item.setQuantity(totalQuantity - freeItems);
                        }
                    });
        }
    }


}
