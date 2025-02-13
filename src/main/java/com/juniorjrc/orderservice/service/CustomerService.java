package com.juniorjrc.orderservice.service;

import com.juniorjrc.ordermodel.entity.Customer;
import com.juniorjrc.ordermodel.exception.OrderServiceNotFoundException;
import com.juniorjrc.orderservice.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private static final String CUSTOMER_NOT_FOUND = "Customer not found!";

    private final CustomerRepository customerRepository;

    public Customer findByIdOrElseThrow(final Long customerId) {
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new OrderServiceNotFoundException(CUSTOMER_NOT_FOUND));
    }
}
