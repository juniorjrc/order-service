package com.juniorjrc.orderservice.service;

import com.juniorjrc.ordermodel.entity.Product;
import com.juniorjrc.orderservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAllByIds(final List<Long> products) {
        return this.productRepository.findAllById(products);
    }
}
