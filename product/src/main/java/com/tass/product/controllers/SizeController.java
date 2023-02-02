package com.tass.product.controllers;

import com.tass.common.customanotation.RequireUserLogin;
import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.product.model.request.SizeRequest;
import com.tass.product.services.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/size")
public class SizeController {
    @Autowired
    SizeService sizeService;

    @GetMapping("/getAllSize")
    public BaseResponseV2 findAllSize() throws ApplicationException {
        return sizeService.findAllSize();
    }

    @GetMapping("/getAllSize/{id}")
    public BaseResponseV2 findSizeById(@PathVariable Long id) throws ApplicationException {
        return sizeService.findSizeById(id);
    }

    @PostMapping("/create")
    public BaseResponseV2 createSize(@RequestBody SizeRequest sizeRequest) throws ApplicationException {
        return sizeService.createSize(sizeRequest);
    }

}
