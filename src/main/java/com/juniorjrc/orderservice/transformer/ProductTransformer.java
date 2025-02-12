package com.juniorjrc.orderservice.transformer;

import com.juniorjrc.ordermodel.dto.ProductDTO;
import com.juniorjrc.ordermodel.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Scope("prototype")
@AllArgsConstructor
public class ProductTransformer {

    public ProductDTO transformToProductDTO(final Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getProductValue(),
                product.getFinalProductValue(),
                product.getSupplier().getName().name()
        );
    }

    public List<ProductDTO> transformAllToProductDTOList(final List<Product> products) {
        return products.stream().map(this::transformToProductDTO).collect(toList());
    }
}
