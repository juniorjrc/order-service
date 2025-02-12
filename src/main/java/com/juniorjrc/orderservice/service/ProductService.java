package com.juniorjrc.orderservice.service;

import com.juniorjrc.ordermodel.dto.CreatesNewOrderProductRequestDTO;
import com.juniorjrc.ordermodel.entity.Product;
import com.juniorjrc.orderservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.juniorjrc.orderservice.validator.ProductValidator.hasProductAndQuantityGreaterThenZero;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProductsByOrder(final List<CreatesNewOrderProductRequestDTO> productsDTO) {
        List<Product> products = new ArrayList<>();
        productsDTO.forEach(productDTO -> {
            Product product = findProductByIdOrElseNull(productDTO.productId());
            if(hasProductAndQuantityGreaterThenZero(product, productDTO)) {
                addProductListInOrder(product, productDTO.quantity(), products);
            }
        });
        return products;
    }

    public Product findProductByIdOrElseNull(final Long productId) {
        return this.productRepository.findById(productId).orElse(null);
    }

    private void addProductListInOrder(final Product product,
                                       final Integer quantity,
                                       final List<Product> products) {
        for(int i = 0; i < quantity; i++) {
            products.add(product);
        }
    }
}
