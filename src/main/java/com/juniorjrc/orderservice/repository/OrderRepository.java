package com.juniorjrc.orderservice.repository;

import com.juniorjrc.ordermodel.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
