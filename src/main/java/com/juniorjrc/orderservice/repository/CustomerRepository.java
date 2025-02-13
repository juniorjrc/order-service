package com.juniorjrc.orderservice.repository;

import com.juniorjrc.ordermodel.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerName(final String customerName);
}
