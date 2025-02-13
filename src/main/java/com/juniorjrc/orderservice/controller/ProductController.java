package com.juniorjrc.orderservice.controller;

import com.juniorjrc.ordermodel.dto.BasicCustomerCheckRequestDTO;
import com.juniorjrc.ordermodel.dto.ProductDTO;
import com.juniorjrc.orderservice.facade.ProductFacade;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private static final String DEFAULT_SIZE = "5";
    private static final String DEFAULT_PAGE = "0";
    private static final String SIZE = "size";
    private static final String PAGE = "page";

    private final ProductFacade productFacade;

    @GetMapping
    public Page<ProductDTO> findAllProducts(
            @RequestBody final BasicCustomerCheckRequestDTO basicCustomerCheckRequestDTO,
            @RequestParam(name = PAGE, required = false, defaultValue = DEFAULT_PAGE) final int page,
            @RequestParam(name = SIZE, required = false, defaultValue = DEFAULT_SIZE) final int size
    ) {
        return this.productFacade.findAll(basicCustomerCheckRequestDTO, page, size);
    }
}
