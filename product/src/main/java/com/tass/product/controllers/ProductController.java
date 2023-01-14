package com.tass.product.controllers;

import com.tass.common.customanotation.RequireUserLogin;
import com.tass.common.model.ApplicationException;

import com.tass.common.model.BaseResponseV2;
import com.tass.common.myenums.ProductStatus;
import com.tass.product.entities.Brand;
import com.tass.product.entities.Category;
import com.tass.product.entities.Product;
import com.tass.product.entities.Size;
import com.tass.product.model.request.ProductRequest;
import com.tass.product.model.request.dto.ProductInfo;
import com.tass.product.services.ProductService;
import com.tass.product.spec.ProductSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/admin/product")

public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    @RequireUserLogin
    public BaseResponseV2 findAllProduct(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "barcode", required = false) String barcode,
                                         @RequestParam(value = "size", required = false) Size size,
                                         @RequestParam(value = "price", required = false) BigDecimal price,
                                         @RequestParam(value = "from", required = false) BigDecimal from,
                                         @RequestParam(value = "to", required = false) BigDecimal to,
                                         @RequestParam(value = "status", required = false) ProductStatus status,
                                         @RequestParam(value = "brand", required = false) Brand brand,
                                         @RequestParam(value = "category", required = false) Category category,
                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws ApplicationException {
        Specification<Product> specification = ProductSpec.productSpec(name, barcode, size, price, from, to, status, brand, category, page, pageSize);
        return productService.findAllProduct(specification, page, pageSize);

    }

    @GetMapping("/{id}")
    @RequireUserLogin
    public BaseResponseV2<ProductInfo> getProductById(@PathVariable Long id) throws ApplicationException {
        return productService.findById(id);
    }

    @PostMapping()
    @RequireUserLogin
    public BaseResponseV2 createProduct(@RequestBody ProductRequest productRequest) throws ApplicationException {
        return productService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    @RequireUserLogin
    public BaseResponseV2 updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) throws ApplicationException {
        return productService.updateProduct(productRequest, id);
    }

    @PutMapping("/delete/{id}")
    @RequireUserLogin
    public BaseResponseV2 deleteProduct(@PathVariable Long id) throws ApplicationException {
        return productService.deleteProduct(id);
    }

    @PutMapping("/active/{id}")
    @RequireUserLogin
    public BaseResponseV2 activeProduct(@PathVariable Long id) throws ApplicationException {
        return productService.activeProduct(id);
    }

}
