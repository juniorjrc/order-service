package com.juniorjrc.orderservice.publisher;

import com.juniorjrc.ordermodel.dto.OrderMessageDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderMessagePublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderToProcessor(final OrderMessageDTO orderMessageDTO) {
        this.rabbitTemplate.convertAndSend(
                orderMessageDTO.exchange(),
                orderMessageDTO.queue(),
                orderMessageDTO.orderId());
    }
}
