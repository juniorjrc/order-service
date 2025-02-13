package com.juniorjrc.orderservice.service;

import com.juniorjrc.ordermodel.dto.BasicCustomerCheckRequestDTO;
import com.juniorjrc.ordermodel.dto.CreateNewOrderRequestDTO;
import com.juniorjrc.ordermodel.dto.UpdateOrderRequestDTO;
import com.juniorjrc.ordermodel.entity.Customer;
import com.juniorjrc.ordermodel.entity.Order;
import com.juniorjrc.ordermodel.entity.Product;
import com.juniorjrc.ordermodel.enums.OrderStatusEnum;
import com.juniorjrc.ordermodel.exception.OrderServiceNotFoundException;
import com.juniorjrc.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.juniorjrc.orderservice.utils.ChecksumUtils.generateCustomerChecksum;
import static com.juniorjrc.orderservice.utils.ChecksumUtils.validateChecksum;

@Service
@AllArgsConstructor
public class OrderService {

    private static final String ORDER_NOT_FOUND = "Order not found!";
    private static final String CREATED_AT_ORDER_ATTRIBUTE = "createdAt";

    private final OrderRepository orderRepository;

    private final CustomerService customerService;
    private final ProductService productService;

    public Page<Order> findAllOrdersByCustomer(final BasicCustomerCheckRequestDTO basicCustomerCheckRequestDTO,
                                               final int page,
                                               final int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(CREATED_AT_ORDER_ATTRIBUTE)));
        final Customer customer = this.customerService.findByIdOrElseThrow(basicCustomerCheckRequestDTO.customerId());
        final String checksum = generateCustomerChecksum(customer);
        validateChecksum(checksum, basicCustomerCheckRequestDTO.checksum());
        return this.orderRepository.findByCustomerId(
                basicCustomerCheckRequestDTO.customerId(),
                pageable);
    }

    public Order findOrderByIdOrElseThrow(final Long orderId) {
        return this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderServiceNotFoundException(ORDER_NOT_FOUND));
    }

    public Order createNewOrder(final CreateNewOrderRequestDTO createNewOrderRequestDTO) {
        final Customer customer = this.customerService.findByIdOrElseThrow(createNewOrderRequestDTO.customerId());
        final String checksum = generateCustomerChecksum(customer);
        validateChecksum(checksum, createNewOrderRequestDTO.checksum());
        List<Product> products = this.productService.getAllProductsByOrder(createNewOrderRequestDTO.products());
        return this.orderRepository.save(Order.createNewOrder(customer, products));
    }

    public void updateOrderStatus(final Long orderId, final OrderStatusEnum statusEnum) {
        Order order = findOrderByIdOrElseThrow(orderId);
        this.orderRepository.save(order.updateOrderStatus(statusEnum));
    }

    public void updateOrderValues(final UpdateOrderRequestDTO updateOrderRequestDTO) {
        Order order = findOrderByIdOrElseThrow(updateOrderRequestDTO.orderId());
        this.orderRepository.save(
                order.updateOrderValues(
                        updateOrderRequestDTO.orderValue(),
                        updateOrderRequestDTO.orderFinalValue(),
                        updateOrderRequestDTO.status()
                ));
    }
}
