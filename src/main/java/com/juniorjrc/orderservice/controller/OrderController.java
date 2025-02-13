package com.juniorjrc.orderservice.controller;

import com.juniorjrc.ordermodel.dto.CreateNewOrderRequestDTO;
import com.juniorjrc.ordermodel.dto.BasicCustomerCheckRequestDTO;
import com.juniorjrc.ordermodel.dto.OrderDTO;
import com.juniorjrc.ordermodel.dto.UpdateOrderRequestDTO;
import com.juniorjrc.ordermodel.dto.UpdateOrderStatusRequestDTO;
import com.juniorjrc.orderservice.facade.OrderFacade;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private static final String DEFAULT_SIZE = "5";
    private static final String DEFAULT_PAGE = "0";
    private static final String SIZE = "size";
    private static final String PAGE = "page";

    private final OrderFacade orderFacade;

    @GetMapping
    public Page<OrderDTO> findAllOrdersByCustomer(
            @RequestBody final BasicCustomerCheckRequestDTO basicCustomerCheckRequestDTO,
            @RequestParam(name = PAGE, required = false, defaultValue = DEFAULT_PAGE) final int page,
            @RequestParam(name = SIZE, required = false, defaultValue = DEFAULT_SIZE) final int size
    ) {
        return this.orderFacade.findAllOrdersByCustomer(basicCustomerCheckRequestDTO, page, size);
    }

    @GetMapping("/{orderId}")
    public OrderDTO findById(@PathVariable final Long orderId) {
        return this.orderFacade.findById(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createNewOrder(@RequestBody CreateNewOrderRequestDTO createNewOrderRequestDTO) {
        return this.orderFacade.createNewOrderAndSendToProcessor(createNewOrderRequestDTO);
    }

    @PutMapping("/status/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(@PathVariable("orderId") final Long orderId,
                                  @RequestBody UpdateOrderStatusRequestDTO updateOrderStatusRequestDTO) {
        this.orderFacade.updateOrderStatus(orderId, updateOrderStatusRequestDTO.status());
    }

    @PostMapping("/update-values")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderValues(@RequestBody UpdateOrderRequestDTO updateOrderRequestDTO) {
        this.orderFacade.updateOrderValues(updateOrderRequestDTO);
    }
}
