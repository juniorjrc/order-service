package com.juniorjrc.orderservice.validator;

import com.juniorjrc.ordermodel.dto.CreatesNewOrderProductRequestDTO;
import com.juniorjrc.ordermodel.entity.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductValidator {

    public static boolean hasProductAndQuantityGreaterThenZero(final Product product,
                                                                final CreatesNewOrderProductRequestDTO productDTO) {
        return nonNull(product) && productDTO.quantity() > 0;
    }

}
