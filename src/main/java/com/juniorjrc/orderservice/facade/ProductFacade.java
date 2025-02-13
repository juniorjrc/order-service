package com.juniorjrc.orderservice.facade;

import com.juniorjrc.ordermodel.dto.BasicCustomerCheckRequestDTO;
import com.juniorjrc.ordermodel.dto.ProductDTO;
import com.juniorjrc.ordermodel.entity.Product;
import com.juniorjrc.orderservice.service.ProductService;
import com.juniorjrc.orderservice.transformer.ProductTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductFacade {

    private final ProductTransformer productTransformer;
    private final ProductService productService;

    public Page<ProductDTO> findAll(final BasicCustomerCheckRequestDTO basicCustomerCheckRequestDTO,
                                    final int page,
                                    final int size) {
        Page<Product> productsPage = this.productService.findAllProducts(basicCustomerCheckRequestDTO, page, size);
        return productsPage.map(this.productTransformer::transformToProductDTO);
    }
}
