package com.tass.product.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponse;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.common.model.userauthen.UserDTO;
import com.tass.common.myenums.BrandStatus;
import com.tass.product.entities.Brand;
import com.tass.product.model.request.BrandRequest;
import com.tass.product.repositories.BrandRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BrandService extends BaseService{
    @Autowired
    BrandRepository brandRepository;

    public BaseResponseV2 findAllBrand() throws ApplicationException {
        return new BaseResponseV2<>(brandRepository.findAll());
    }
    UserDTO userDTO = getUserDTO();

    public BaseResponseV2 createBrand(BrandRequest brandRequest) throws ApplicationException {
        validateRequestCreateException(brandRequest);
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setImage(brandRequest.getImage());
        brand.setStatus(BrandStatus.ACTIVE);
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brandRepository.save(brand);
        return new BaseResponseV2(brand);
    }

    public BaseResponseV2 updateBrand(BrandRequest brandRequest, Long id) throws ApplicationException {
        validateRequestCreateException(brandRequest);
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Brand not found!");
        }
        Brand existBrand = optionalBrand.get();
        existBrand.setName(brandRequest.getName());
        existBrand.setImage(brandRequest.getImage());
        existBrand.setStatus(brandRequest.getStatus());
        existBrand.setCreatedAt(LocalDateTime.now());
        existBrand.setUpdatedAt(LocalDateTime.now());
        brandRepository.save(existBrand);
        return new BaseResponseV2(existBrand);
    }

    public BaseResponseV2 findBrandById(Long id) throws ApplicationException {
        return new BaseResponseV2(brandRepository.findById(id));
    }
    public BaseResponseV2 deleteBrand(Long id) throws ApplicationException {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Brand not found!");
        }
        Brand existBrand = optionalBrand.get();
        existBrand.setStatus(BrandStatus.DEACTIVE);
        brandRepository.save(existBrand);
        return  new BaseResponseV2();
    }
    public BaseResponseV2 activeBrand(Long id) throws ApplicationException {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Brand not found!");
        }
        Brand existBrand = optionalBrand.get();
        existBrand.setStatus(BrandStatus.ACTIVE);
        brandRepository.save(existBrand);
        return  new BaseResponseV2();
    }
    public void validateRequestCreateException(BrandRequest brandRequest) throws ApplicationException {
        if (StringUtils.isBlank(brandRequest.getName())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "name is blank");
        }
        if (StringUtils.isBlank(brandRequest.getImage())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Image is blank");
        }
    }
}
