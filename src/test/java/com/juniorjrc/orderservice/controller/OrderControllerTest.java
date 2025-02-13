package com.juniorjrc.orderservice.controller;

import com.juniorjrc.ordermodel.dto.BasicCustomerCheckRequestDTO;
import com.juniorjrc.ordermodel.dto.CreateNewOrderProductRequestDTO;
import com.juniorjrc.ordermodel.dto.CreateNewOrderRequestDTO;
import com.juniorjrc.ordermodel.dto.OrderDTO;
import com.juniorjrc.ordermodel.dto.UpdateOrderRequestDTO;
import com.juniorjrc.ordermodel.dto.UpdateOrderStatusRequestDTO;
import com.juniorjrc.ordermodel.entity.Order;
import com.juniorjrc.orderservice.annotations.IntegrationTest;
import com.juniorjrc.orderservice.repository.OrderRepository;
import com.juniorjrc.orderservice.utils.OrderServiceMockHttpRequestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import static com.juniorjrc.ordermodel.enums.OrderStatusEnum.PROCESSING;
import static com.juniorjrc.orderservice.utils.OrderServiceHttpMethodUtils.GET;
import static com.juniorjrc.orderservice.utils.OrderServiceHttpMethodUtils.POST;
import static com.juniorjrc.orderservice.utils.OrderServiceHttpMethodUtils.PUT;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@IntegrationTest
@Sql("classpath:sql/truncate.sql")
@Sql("classpath:sql/initial-data.sql")
@AutoConfigureMockMvc
class OrderControllerTest extends OrderServiceMockHttpRequestUtils {

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void mustGetAllOrdersCorrectly() throws Exception {
        final BasicCustomerCheckRequestDTO basicCustomerCheckRequestDTO = new BasicCustomerCheckRequestDTO(
                1L,
                "jsggLbwb2rCY4TQsPUIk0FNJNmEQ1bgQfA7R8iy1638="
        );
        String jsonResponse = executeHttpRequest(
                GET,
                "/order",
                basicCustomerCheckRequestDTO,
                new HashMap<>()
        ).getResponse().getContentAsString();

        final Page<OrderDTO> ordersPage = fromJsonToPage(jsonResponse, OrderDTO.class);
        assertEquals(BigDecimal.valueOf(5.0).setScale(2, RoundingMode.HALF_UP), ordersPage.getContent().getFirst().orderValue().setScale(2, RoundingMode.HALF_UP));
        assertEquals("Adega da Luz", ordersPage.getContent().getFirst().customerName());
    }

    @Test
    void mustThrowBadRequestWhenChecksumIsInvalid() throws Exception {
        final BasicCustomerCheckRequestDTO basicCustomerCheckRequestDTO = new BasicCustomerCheckRequestDTO(
                1L,
                "123"
        );
        MvcResult result = executeHttpRequest(
                GET,
                "/order",
                basicCustomerCheckRequestDTO,
                new HashMap<>()
        );
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    void mustFindByIdCorrectly() throws Exception {
        String jsonResponse = executeHttpRequest(
                GET,
                "/order/1000",
                null,
                new HashMap<>()
        ).getResponse().getContentAsString();

        final OrderDTO orderDTO = fromJson(jsonResponse, OrderDTO.class);
        assertEquals(BigDecimal.valueOf(5.0).setScale(2, RoundingMode.HALF_UP), orderDTO.orderValue());
        assertEquals("Adega da Luz", orderDTO.customerName());
    }

    @Test
    void mustThrowBadRequestWhenOrderNotFound() throws Exception {
        MvcResult result = executeHttpRequest(
                GET,
                "/order/10000",
                null,
                new HashMap<>()
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    void mustCreateNewOrderCorrectly() throws Exception {
        final CreateNewOrderProductRequestDTO createNewOrderProductRequestDTO = new CreateNewOrderProductRequestDTO(
                1L,
                10
        );
        final CreateNewOrderRequestDTO createNewOrderRequestDTO = new CreateNewOrderRequestDTO(
                1L,
                "jsggLbwb2rCY4TQsPUIk0FNJNmEQ1bgQfA7R8iy1638=",
                singletonList(createNewOrderProductRequestDTO)
        );
        MvcResult result = executeHttpRequest(
                POST,
                "/order",
                createNewOrderRequestDTO,
                new HashMap<>()
        );

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        List<Order> orderList = orderRepository.findAll();
        assertEquals(2, orderList.size());
    }

    @Test
    void mustUpdateOrderStatusCorrectly() throws Exception {
        final UpdateOrderStatusRequestDTO updateOrderStatusRequestDTO = new UpdateOrderStatusRequestDTO(
                PROCESSING,
                null
        );

        executeHttpRequest(
                PUT,
                "/order/status/1000",
                updateOrderStatusRequestDTO,
                new HashMap<>()
        );

        Order order = this.orderRepository.findById(1000L).orElse(null);
        if (nonNull(order)) {
            assertEquals(PROCESSING, order.getStatus());
        }
    }

    @Test
    void mustUpdateValuesCorrectly() throws Exception {
        final UpdateOrderRequestDTO updateOrderRequestDTO = new UpdateOrderRequestDTO(
                1000L,
                BigDecimal.valueOf(25),
                BigDecimal.valueOf(25),
                PROCESSING
        );

        executeHttpRequest(
                POST,
                "/order/update-values",
                updateOrderRequestDTO,
                new HashMap<>()
        );

        Order order = this.orderRepository.findById(1000L).orElse(null);
        if (nonNull(order)) {
            assertEquals(PROCESSING, order.getStatus());
            assertEquals(BigDecimal.valueOf(25.0).setScale(2, RoundingMode.HALF_UP), order.getOrderValue());
            assertEquals(BigDecimal.valueOf(25.0).setScale(2, RoundingMode.HALF_UP), order.getOrderFinalValue());
        }
    }
}