package com.juniorjrc.orderservice.transformer;

import com.juniorjrc.ordermodel.dto.OrderDTO;
import com.juniorjrc.ordermodel.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@AllArgsConstructor
public class OrderTransformer {

    private final ProductTransformer productTransformer;

    public OrderDTO transformToOrderDTO(final Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCustomer().getCustomerName(),
                order.getOrderValue(),
                order.getOrderFinalValue(),
                order.getStatus(),
                order.getErrorDetails(),
                this.productTransformer.transformAllToProductDTOList(order.getProducts())
        );
    }
}
