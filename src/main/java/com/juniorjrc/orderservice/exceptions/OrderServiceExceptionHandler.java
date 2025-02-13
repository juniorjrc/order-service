package com.juniorjrc.orderservice.exceptions;

import com.juniorjrc.ordermodel.exception.OrderServiceException;
import com.juniorjrc.ordermodel.exception.OrderServiceExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.juniorjrc.ordermodel.enums.OrderServiceExceptionEnum.ORDER_SERVICE_INTERNAL_SERVER_ERROR;
import static java.util.Objects.nonNull;

@Slf4j
@ControllerAdvice
@Primary
public class OrderServiceExceptionHandler {

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<OrderServiceExceptionResponse> handleByCode(final Throwable throwable) {
        final boolean isCustomException = throwable instanceof OrderServiceException;

        final HttpStatus responseHttpStatus = isCustomException ?
                ((OrderServiceException) throwable).getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(buildResponse(throwable, isCustomException), responseHttpStatus);
    }

    private OrderServiceExceptionResponse buildResponse(final Throwable throwable,
                                                        final boolean isCustomException) {
        String code = ORDER_SERVICE_INTERNAL_SERVER_ERROR.name();
        Map<String, String> errors = new HashMap<>();

        if (isCustomException && nonNull(((OrderServiceException) throwable).getCode())) {
            code = ((OrderServiceException) throwable).getCode().name();
        }

        if (throwable instanceof MethodArgumentNotValidException) {
            errors = ((MethodArgumentNotValidException) throwable).getBindingResult().getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        }

        final OrderServiceExceptionResponse response = new OrderServiceExceptionResponse(
                code,
                throwable.getMessage(),
                nonNull(throwable.getCause()) ? throwable.getCause().getMessage() : "",
                errors
        );

        log.error(response.code(), throwable);
        return response;
    }
}
