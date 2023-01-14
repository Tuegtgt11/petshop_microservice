package com.tass.product.controllers;

import com.tass.common.customanotation.RequireUserLogin;
import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponse;
import com.tass.common.model.BaseResponseV2;
import com.tass.product.model.request.BrandRequest;
import com.tass.product.repositories.BrandRepository;
import com.tass.product.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    @GetMapping
    public BaseResponseV2 findAllBrand() throws ApplicationException {
        return brandService.findAllBrand();
    }

    @GetMapping("/{id}")
    public BaseResponseV2 findBrandById(@PathVariable Long id) throws ApplicationException {
        return brandService.findBrandById(id);
    }

    @PostMapping("/create")
    public BaseResponseV2 createBrand(@RequestBody BrandRequest brandRequest) throws ApplicationException {
        return brandService.createBrand(brandRequest);
    }

    @PutMapping("/update/{id}")
    public BaseResponseV2 updateBrand(@RequestBody BrandRequest brandRequest, @PathVariable Long id) throws ApplicationException {
        return brandService.updateBrand(brandRequest, id);
    }

    @PutMapping("/delete/{id}")
    public BaseResponseV2 deleteBrand(@PathVariable Long id) throws  ApplicationException {
        return brandService.deleteBrand(id);
    }

    @PutMapping("/active/{id}")
    public BaseResponseV2 activeBrand(@PathVariable Long id) throws ApplicationException {
        return brandService.activeBrand(id);
    }
}
