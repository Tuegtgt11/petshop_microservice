package com.tass.product.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponse;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.common.myenums.CategoryStatus;
import com.tass.common.myenums.ProductStatus;
import com.tass.product.entities.Category;
import com.tass.product.entities.Product;
import com.tass.product.model.request.CategoryRequest;
import com.tass.product.repositories.CategoryRepository;
import com.tass.product.repositories.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    public BaseResponseV2 findAllCategory() throws ApplicationException {
        return new BaseResponseV2(categoryRepository.findAll());
    }

    public BaseResponseV2 findCategoryById(Long id) throws ApplicationException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Category not found!");
        }
        return new BaseResponseV2(optionalCategory.get());
    }

    public BaseResponseV2 createCategory(CategoryRequest categoryRequest) throws ApplicationException {
        validateRequestCreateException(categoryRequest);
        Category category = new Category();
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryRequest.getName());
        if (optionalCategory.isPresent()){
            throw new ApplicationException(ERROR.INVALID_PARAM, "Category Name already exist!");
        }
        category.setName(categoryRequest.getName());
        category.setIcon(categoryRequest.getIcon());
        category.setStatus(categoryRequest.getStatus());
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
        return new BaseResponseV2(category);
    }

    public BaseResponseV2 updateCategory(CategoryRequest categoryRequest, Long id) throws ApplicationException {
        validateRequestCreateException(categoryRequest);
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Category not found!");
        }

        Category existCategory = optionalCategory.get();
        existCategory.setName(categoryRequest.getName());
        existCategory.setIcon(categoryRequest.getIcon());
        existCategory.setStatus(categoryRequest.getStatus());
        existCategory.setCreatedAt(LocalDateTime.now());
        existCategory.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(existCategory);
        return new BaseResponseV2(existCategory);
    }

    public BaseResponseV2 deleteCategory(Long id) throws ApplicationException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Category not found!");
        }
        Category existCategory = optionalCategory.get();
        existCategory.setStatus(CategoryStatus.DEACTIVE);
        categoryRepository.save(existCategory);

        List<Product> productList = productRepository.findAllByCategory(existCategory);
        for (var i = 0; i < productList.size(); i++) {
            productList.get(i).setStatus(ProductStatus.DELETED);
        }
        productRepository.saveAll(productList);
        return new BaseResponseV2();

    }

    public BaseResponseV2 activeCategory(Long id) throws ApplicationException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Category not found!");
        }
        Category existCategory = optionalCategory.get();
        existCategory.setStatus(CategoryStatus.ACTIVE);
        categoryRepository.save(existCategory);
        return new BaseResponseV2();

    }

    private void validateRequestCreateException(CategoryRequest categoryRequest) throws ApplicationException {

        if (StringUtils.isBlank(categoryRequest.getName())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Name is blank!");
        }

        if (StringUtils.isBlank(categoryRequest.getIcon())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Description is blank!");
        }

        if (StringUtils.isBlank(categoryRequest.getStatus().toString())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Status is Blank!");
        }

    }

}
