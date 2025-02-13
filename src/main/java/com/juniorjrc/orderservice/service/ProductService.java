package com.juniorjrc.orderservice.service;

import com.juniorjrc.ordermodel.dto.BasicCustomerCheckRequestDTO;
import com.juniorjrc.ordermodel.dto.CreateNewOrderProductRequestDTO;
import com.juniorjrc.ordermodel.entity.Customer;
import com.juniorjrc.ordermodel.entity.Product;
import com.juniorjrc.orderservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.juniorjrc.orderservice.utils.ChecksumUtils.generateCustomerChecksum;
import static com.juniorjrc.orderservice.utils.ChecksumUtils.validateChecksum;
import static com.juniorjrc.orderservice.validator.ProductValidator.hasProductAndQuantityGreaterThenZero;

@Service
@AllArgsConstructor
public class ProductService {

    private static final String PRODUCT_NAME_ORDER_ATTRIBUTE = "name";

    private final ProductRepository productRepository;
    private final CustomerService customerService;

    public Page<Product> findAllProducts(final BasicCustomerCheckRequestDTO basicCustomerCheckRequestDTO,
                                         final int page,
                                         final int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(PRODUCT_NAME_ORDER_ATTRIBUTE)));
        final Customer customer = this.customerService.findByIdOrElseThrow(basicCustomerCheckRequestDTO.customerId());
        final String checksum = generateCustomerChecksum(customer);
        validateChecksum(checksum, basicCustomerCheckRequestDTO.checksum());
        return this.productRepository.findAll(pageable);
    }

    public List<Product> getAllProductsByOrder(final List<CreateNewOrderProductRequestDTO> productsDTO) {
        List<Product> products = new ArrayList<>();
        productsDTO.forEach(productDTO -> {
            Product product = findProductByIdOrElseNull(productDTO.productId());
            if (hasProductAndQuantityGreaterThenZero(product, productDTO)) {
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
        for (int i = 0; i < quantity; i++) {
            products.add(product);
        }
    }
}
