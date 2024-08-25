package com.monk.entity;

import com.monk.model.CouponDetails;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "coupons")
@Data
public class Coupon {
    @Id
    private String id;
    private String type;
    private CouponDetails couponDetails;
    private Date expirationDate;
}
