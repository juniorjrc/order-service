package com.juniorjrc.orderservice.repository;

import com.juniorjrc.ordermodel.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
