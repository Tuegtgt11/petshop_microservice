package com.tass.product.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponse;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.product.entities.Category;
import com.tass.product.entities.myenum.CategoryStatus;
import com.tass.product.model.request.CategoryRequest;
import com.tass.product.repositories.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

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

    public BaseResponseV2 updateCategory(CategoryRequest categoryRequest, Principal principal, Long id) throws ApplicationException {
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

    public BaseResponse deleteCategory(Long id) throws ApplicationException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Category not found!");
        }
        Category existCategory = optionalCategory.get();
        existCategory.setStatus(CategoryStatus.DELETED);
        categoryRepository.save(existCategory);
        return new BaseResponse(200, "Deleted!");

    }

    public BaseResponse activeCategory(Long id) throws ApplicationException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Category not found!");
        }
        Category existCategory = optionalCategory.get();
        existCategory.setStatus(CategoryStatus.ACTIVE);
        categoryRepository.save(existCategory);
        return new BaseResponse(200, "Active!");

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
