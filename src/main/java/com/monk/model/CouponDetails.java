package com.monk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponDetails {
    private Integer threshold;
    private Integer discount;
    private List<BuyProduct> buyProducts;
    private List<GetProduct> getProducts;
    private Integer repitionLimit;
}
