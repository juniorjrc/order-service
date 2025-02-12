package com.juniorjrc.orderservice.facade;

import com.juniorjrc.ordermodel.dto.CreateNewOrderRequestDTO;
import com.juniorjrc.ordermodel.dto.OrderDTO;
import com.juniorjrc.orderservice.service.OrderService;
import com.juniorjrc.orderservice.transformer.OrderTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderTransformer orderTransformer;

    public OrderDTO findById(final Long orderId) {
        return this.orderTransformer.transformToOrderDTO(
                this.orderService.findOrderByIdOrElseThrow(orderId));
    }

    public OrderDTO createNewOrderAndSendToProcessor(final CreateNewOrderRequestDTO createNewOrderRequestDTO) {
        return this.orderTransformer.transformToOrderDTO(this.orderService.createNewOrder(createNewOrderRequestDTO));
    }
}
