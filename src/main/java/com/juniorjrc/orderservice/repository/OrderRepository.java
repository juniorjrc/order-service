package com.juniorjrc.orderservice.repository;

import com.juniorjrc.ordermodel.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(final Long customerId, final Pageable pageable);
}
