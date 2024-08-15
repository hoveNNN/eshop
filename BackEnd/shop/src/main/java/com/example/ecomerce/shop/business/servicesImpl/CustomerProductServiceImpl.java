package com.example.ecomerce.shop.business.servicesImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.CustomerProductService;
import com.example.ecomerce.shop.dao.entity.FAQ;
import com.example.ecomerce.shop.dao.entity.Product;
import com.example.ecomerce.shop.dao.entity.Review;
import com.example.ecomerce.shop.dao.repository.FAQRepository;
import com.example.ecomerce.shop.dao.repository.ProductRepository;
import com.example.ecomerce.shop.dao.repository.ReviewRepository;
import com.example.ecomerce.shop.web.dto.ProductDetailDto;
import com.example.ecomerce.shop.web.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl  implements CustomerProductService  {

    private final ProductRepository productRepository;
    private final FAQRepository faqRepository;
    private final ReviewRepository reviewRepository;

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public List<ProductDto> getProductByName(String name) {
        List<Product> products = productRepository.findByName(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public ProductDetailDto getProductDetailId(Long productId){
        Optional <Product> optionalProduct= productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            List<FAQ> faqList= faqRepository.findAllByProductId(productId);
            List<Review> reviewList= reviewRepository.findAllByProductId(productId);

            ProductDetailDto productDetailDto= new ProductDetailDto();

            productDetailDto.setProductDto(optionalProduct.get().getDto());
            productDetailDto.setFaqDtoList(faqList.stream().map(FAQ::getFAQDto).collect(Collectors.toList()));
            productDetailDto.setReviewDtoList(reviewList.stream().map(Review::getDto).collect(Collectors.toList()));
            return  productDetailDto;
        }
        return null;
    }
}