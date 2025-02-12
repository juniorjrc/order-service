package com.juniorjrc.orderservice.service;

import com.juniorjrc.ordermodel.dto.CreateNewOrderRequestDTO;
import com.juniorjrc.ordermodel.entity.Customer;
import com.juniorjrc.ordermodel.entity.Order;
import com.juniorjrc.ordermodel.entity.Product;
import com.juniorjrc.ordermodel.enums.OrderStatusEnum;
import com.juniorjrc.ordermodel.exception.OrderServiceBadRequestException;
import com.juniorjrc.ordermodel.exception.OrderServiceInternalServerErrorException;
import com.juniorjrc.ordermodel.exception.OrderServiceNotFoundException;
import com.juniorjrc.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private static final String ORDER_NOT_FOUND = "Order not found!";
    private static final String INTERNAL_SERVER_ERROR_IN_CHECKSUM = "Internal system error when generating checksum, try again later!";
    private static final String CHECKSUM_ALGORITHM = "SHA-256";
    private static final String INVALID_REQUEST = "Invalid request!";

    private final OrderRepository orderRepository;

    private final CustomerService customerService;
    private final ProductService productService;

    public Order findOrderByIdOrElseThrow(final Long orderId) {
        return this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderServiceNotFoundException(ORDER_NOT_FOUND));
    }

    public Order createNewOrder(final CreateNewOrderRequestDTO createNewOrderRequestDTO) {
        final Customer customer = this.customerService.findByIdOrElseThrow(createNewOrderRequestDTO.customerId());
        final String checksum = generateCustomerChecksum(customer);
        if(!checksum.equals(createNewOrderRequestDTO.checksum())) {
            throw new OrderServiceBadRequestException(INVALID_REQUEST);
        }
        List<Product> products = this.productService.getAllProductsByOrder(createNewOrderRequestDTO.products());
        return this.orderRepository.save(Order.createNewOrder(customer, products));
    }

    public void updateOrderStatus(final Long orderId, final OrderStatusEnum statusEnum) {
        Order order = findOrderByIdOrElseThrow(orderId);
        this.orderRepository.save(order.updateOrderStatus(statusEnum));
    }

    private static String generateCustomerChecksum(final Customer customer) {
        try {
            String input = customer.getPrivateKey() + customer.getId();
            MessageDigest digest = MessageDigest.getInstance(CHECKSUM_ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new OrderServiceInternalServerErrorException(INTERNAL_SERVER_ERROR_IN_CHECKSUM);
        }
    }
}
