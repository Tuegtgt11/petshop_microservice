package com.tass.product.controllers;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.dto.product.ProductDTO;
import com.tass.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public BaseResponseV2<ProductDTO> getProductById(@PathVariable Long id) throws ApplicationException {
        return productService.findById(id);
    }

}
