package com.tass.product.controllers;

import com.tass.common.customanotation.RequireUserLogin;
import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.product.model.request.CategoryRequest;
import com.tass.product.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public BaseResponseV2 findAllCategory() throws ApplicationException {
        return categoryService.findAllCategory();
    }

    @GetMapping("/{id}")
    public BaseResponseV2 findCategoryById(@PathVariable Long id) throws ApplicationException {
        return categoryService.findCategoryById(id);
    }

    @PostMapping("/create")
    public BaseResponseV2 createCategory(@RequestBody CategoryRequest categoryRequest) throws ApplicationException {
        return categoryService.createCategory(categoryRequest);
    }

    @PutMapping("/update/{id}")
    public BaseResponseV2 updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable Long id) throws ApplicationException {
        return categoryService.updateCategory(categoryRequest, id);
    }

    @PutMapping("/delete/{id}")
    public BaseResponseV2 deleteCategory(@PathVariable Long id) throws ApplicationException {
        return categoryService.deleteCategory(id);
    }

    @PutMapping("/active/{id}")
    public BaseResponseV2 activeCategory(@PathVariable Long id) throws ApplicationException {
        return categoryService.activeCategory(id);
    }


}
