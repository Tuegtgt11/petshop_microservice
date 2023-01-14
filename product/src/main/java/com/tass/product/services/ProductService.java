package com.tass.product.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.common.model.dto.order.OrderDTO;
import com.tass.common.myenums.ProductStatus;
import com.tass.product.entities.Brand;
import com.tass.product.entities.Category;
import com.tass.product.entities.Product;
import com.tass.product.entities.Size;
import com.tass.product.model.request.ProductRequest;
import com.tass.product.model.request.dto.ProductInfo;
import com.tass.product.repositories.BrandRepository;
import com.tass.product.repositories.CategoryRepository;
import com.tass.product.repositories.ProductRepository;
import com.tass.product.repositories.SizeRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BrandRepository brandRepository;

    public BaseResponseV2 findAllProduct(Specification<Product> specification, Integer page, Integer pageSize) throws ApplicationException {
        return new BaseResponseV2(productRepository.findAll(specification, PageRequest.of(page, pageSize, Sort.by("updatedAt").descending())));
    }


    public BaseResponseV2 createProduct(ProductRequest productRequest) throws ApplicationException {
        validateRequestCreateException(productRequest);
        Product product = new Product();
        product.setName(productRequest.getName());

        Optional<Category> optionalCategory = categoryRepository.findById(productRequest.getCategory().getId());
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Category not found!");
        }
        product.setCategory(optionalCategory.get());
        Optional<Brand> optionalBrand = brandRepository.findById(productRequest.getBrand().getId());
        if (optionalBrand.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Brand not found!");
        }
        product.setBrand(optionalBrand.get());
        Optional<Size> optionalSize = sizeRepository.findById(productRequest.getSize().getId());
        if (optionalSize.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Size not found!");
        }
        product.setSize(optionalSize.get());
        product.setDescription(productRequest.getDescription());
        product.setDetail(productRequest.getDetail());
        product.setImages(productRequest.getImages());
        Optional<Product> optionalProductBarcode = productRepository.findProductByBarcode(productRequest.getBarcode());
        if (optionalProductBarcode.isPresent()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Barcode already exist!");
        }
        product.setBarcode(productRequest.getBarcode());
        product.setPrice(productRequest.getPrice());
        product.setSold(0);
        product.setQuantity(productRequest.getQuantity());
        product.setStatus(productRequest.getStatus());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        return new BaseResponseV2(product);
    }

    public BaseResponseV2<ProductInfo> updateProduct(ProductRequest productRequest,Long id) throws ApplicationException {
        validateRequestCreateException(productRequest);
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(productRequest.getCategory().getId());
        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Category not found!");
        }

        Optional<Brand> optionalBrand = brandRepository.findById(productRequest.getBrand().getId());
        if (optionalBrand.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Brand not found!");
        }

        Optional<Size> optionalSize = sizeRepository.findById(productRequest.getSize().getId());
        if (optionalSize.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Size not found!");
        }

        Product existProduct = productOptional.get();
        existProduct.setName(productRequest.getName());
        existProduct.setCategory(optionalCategory.get());
        existProduct.setBrand(optionalBrand.get());
        existProduct.setSize(optionalSize.get());
        existProduct.setDescription(productRequest.getDescription());
        Optional<Product> optionalProductBarcode = productRepository.findProductByBarcode(productRequest.getBarcode());
        if (optionalProductBarcode.isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Barcode not exist!");
        }
        existProduct.setDetail(productRequest.getDetail());
        existProduct.setImages(productRequest.getImages());
        existProduct.setPrice(productRequest.getPrice());
        existProduct.setSold(productRequest.getSold());
        existProduct.setQuantity(productRequest.getQuantity());
        existProduct.setStatus(productRequest.getStatus());
        existProduct.setCreatedAt(LocalDateTime.now());
        existProduct.setUpdatedAt(LocalDateTime.now());
        productRepository.save(existProduct);
        return new BaseResponseV2(existProduct);
    }

    public BaseResponseV2<ProductInfo> findById(Long id) throws ApplicationException {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR);
        }
        Product product = productOptional.get();
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(product, productInfo);
        BaseResponseV2 response = new BaseResponseV2<>(product);
        return response;
    }

    public BaseResponseV2 deleteProduct(Long id) throws ApplicationException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Product not found!");
        }
        Product existProduct = optionalProduct.get();
        existProduct.setStatus(ProductStatus.DELETED);
        productRepository.save(existProduct);
        return new BaseResponseV2();

    }

    public BaseResponseV2 activeProduct(Long id) throws ApplicationException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR, "Product not found!");
        }
        Product existProduct = optionalProduct.get();
        existProduct.setStatus(ProductStatus.ACTIVE);
        productRepository.save(existProduct);
        return new BaseResponseV2();

    }

    private void validateRequestCreateException(ProductRequest productRequest) throws ApplicationException {

        if (StringUtils.isBlank(productRequest.getName())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "name is blank");
        }

        if (StringUtils.isBlank(productRequest.getDescription())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Description is blank");
        }

        if (StringUtils.isBlank(productRequest.getImages())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "image is Blank");
        }
        if (StringUtils.isBlank(productRequest.getDetail())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "detail is Blank");
        }
        if (StringUtils.isBlank(productRequest.getDescription())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "description is Blank");
        }
        if (StringUtils.isBlank(productRequest.getCategory().getId().toString())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "category is Blank");
        }
        if (StringUtils.isBlank(productRequest.getBrand().getId().toString())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "brand is Blank");
        }
        if (StringUtils.isBlank(productRequest.getSize().toString())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Size is Blank");
        }
        if (StringUtils.isBlank(productRequest.getPrice().toString())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Price is Blank");
        }

    }

    public BaseResponseV2 callbackProduct(Long id,int qty)throws ApplicationException{
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Products not found");
        }
        Product product = optionalProduct.get();
        product.setQuantity(product.getQuantity()-qty);
        product.setSold(qty);
        productRepository.save(product);

        return new BaseResponseV2<>(product);
    }
    @RabbitListener(queues = {"${spring.rabbitmq.queue.product}"})
    private void listenMessage(OrderDTO orderDTO){
        log.info("data " + orderDTO.getProductId());
        callbackProduct(orderDTO.getProductId(),orderDTO.getQty());
    }
}
