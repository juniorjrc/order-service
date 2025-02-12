package com.juniorjrc.orderservice.facade;

import com.juniorjrc.ordermodel.dto.CreateNewOrderRequestDTO;
import com.juniorjrc.ordermodel.dto.OrderDTO;
import com.juniorjrc.ordermodel.dto.OrderMessageDTO;
import com.juniorjrc.ordermodel.entity.Order;
import com.juniorjrc.ordermodel.enums.OrderStatusEnum;
import com.juniorjrc.orderservice.publisher.OrderMessagePublisherService;
import com.juniorjrc.orderservice.service.OrderService;
import com.juniorjrc.orderservice.transformer.OrderTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.juniorjrc.ordermodel.constants.OrderMessageQueue.ORDER_PROCESSOR_EXCHANGE_NAME;
import static com.juniorjrc.ordermodel.constants.OrderMessageQueue.ORDER_PROCESSOR_QUEUE;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderTransformer orderTransformer;
    private final OrderMessagePublisherService orderMessagePublisherService;

    public OrderDTO findById(final Long orderId) {
        return this.orderTransformer.transformToOrderDTO(
                this.orderService.findOrderByIdOrElseThrow(orderId));
    }

    @Transactional
    public OrderDTO createNewOrderAndSendToProcessor(final CreateNewOrderRequestDTO createNewOrderRequestDTO) {
        final Order order = this.orderService.createNewOrder(createNewOrderRequestDTO);
        this.orderMessagePublisherService.publishOrderToProcessor(new OrderMessageDTO(
                order.getId(),
                ORDER_PROCESSOR_EXCHANGE_NAME,
                ORDER_PROCESSOR_QUEUE
        ));
        return this.orderTransformer.transformToOrderDTO(order);
    }

    @Transactional
    public void updateOrderStatus(final Long orderId, final OrderStatusEnum statusEnum) {
        this.orderService.updateOrderStatus(orderId, statusEnum);
    }
}
