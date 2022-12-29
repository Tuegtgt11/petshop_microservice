package com.tass.product.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.common.model.dto.product.ProductDTO;
import com.tass.product.entities.Product;
import com.tass.product.repositories.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public BaseResponseV2<ProductDTO> findById(long id) throws ApplicationException{

        log.info("get product info by id {}" , id);

        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isEmpty()){
            log.debug("not found product with id {}" , id);
            throw new ApplicationException(ERROR.ID_NOT_FOUND);
        }

        ProductDTO productDTO = new ProductDTO();

        Product product = productOpt.get();

        BeanUtils.copyProperties(product, productDTO);


        BaseResponseV2<ProductDTO> response = new BaseResponseV2<>();

        response.setData(productDTO);

        return response;

    }
}
