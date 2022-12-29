package com.tass.product.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponse;
import com.tass.common.model.BaseResponseV2;
import com.tass.product.entities.Size;
import com.tass.product.model.request.SizeRequest;
import com.tass.product.repositories.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tass.product.entities.Size;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SizeService {
    @Autowired
    SizeRepository sizeRepository;
    public BaseResponseV2 findAllSize() throws ApplicationException {
        return new BaseResponseV2(sizeRepository.findAll());
    }

    public BaseResponseV2 findSizeById(Long id) throws ApplicationException {
        return new BaseResponseV2(sizeRepository.findById(id));
    }

    public BaseResponseV2 createSize(SizeRequest sizeRequest) throws ApplicationException{
        Size size = new Size();
        size.setName(sizeRequest.getName());
        size.setWeight(sizeRequest.getWeight());
        size.setCreatedAt(LocalDateTime.now());
        size.setUpdatedAt(LocalDateTime.now());
        sizeRepository.save(size);
        return new BaseResponseV2(size);
    }

//    public BaseResponse updateSize(SizeRequest sizeRequest, Principal principal, Long id) throws ApiException {
//        Optional<Size> optionalSize = sizeRepository.findById(id);
//        Optional<User> user = userRepository.findByUsernameAndStatus(principal.getName(), UserStatus.ACTIVE);
//        if (optionalSize.isEmpty()){
//            throw new ApiException(ERROR.SYSTEM_ERROR, "Size not found!");
//        }
//        Size existSize = optionalSize.get();
//        existSize.setName(sizeRequest.getName());
//        existSize.setWeight(sizeRequest.getWeight());
//        existSize.setCreatedAt(LocalDateTime.now());
//        existSize.setUpdatedAt(LocalDateTime.now());
//        existSize.setCreatedBy(user.get().getUsername());
//        existSize.setCreatedBy(user.get().getUsername());
//        sizeRepository.save(existSize);
//        return new BaseResponse(200 , "Cuccess!", existSize);
//    }
//    public BaseResponse deleteSize(Long id) throws ApiException {
//        Optional<Size> sizeOptional = sizeRepository.findById(id);
//        if (sizeOptional.isEmpty()){
//            throw new ApiException(ERROR.SYSTEM_ERROR);
//        }
//         sizeRepository.delete(sizeOptional.get());
//        return new BaseResponse(200, "Success");
//    }
}
