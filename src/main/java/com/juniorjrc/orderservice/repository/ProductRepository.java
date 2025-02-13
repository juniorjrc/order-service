package com.juniorjrc.orderservice.repository;

import com.juniorjrc.ordermodel.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @NonNull
    @Override
    Page<Product> findAll(@NonNull final Pageable pageable);
}
