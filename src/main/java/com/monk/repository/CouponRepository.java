package com.monk.repository;

import com.monk.entity.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CouponRepository extends MongoRepository<Coupon, String> {
}
