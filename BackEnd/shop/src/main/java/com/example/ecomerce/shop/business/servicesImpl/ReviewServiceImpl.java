package com.example.ecomerce.shop.business.servicesImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.ReviewService;
import com.example.ecomerce.shop.dao.entity.CartItems;
import com.example.ecomerce.shop.dao.entity.Order;
import com.example.ecomerce.shop.dao.entity.Product;
import com.example.ecomerce.shop.dao.entity.Review;
import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.repository.OrderRepository;
import com.example.ecomerce.shop.dao.repository.ProductRepository;
import com.example.ecomerce.shop.dao.repository.ReviewRepository;
import com.example.ecomerce.shop.dao.repository.UserRepository;
import com.example.ecomerce.shop.web.dto.OrderedProductsResponseDto;
import com.example.ecomerce.shop.web.dto.ProductDto;
import com.example.ecomerce.shop.web.dto.ReviewDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long  orderId) {
        Optional <Order> optionalOrder=orderRepository.findById(orderId);
        OrderedProductsResponseDto orderedProductsResponseDto=new OrderedProductsResponseDto();
        if (optionalOrder.isPresent()) {
            orderedProductsResponseDto.setOrderAmount(optionalOrder.get().getAmount());
            List<ProductDto> productDtoList= new ArrayList<>();
            for(CartItems cartItems:optionalOrder.get().getCartItems()){
                ProductDto  productDto= new ProductDto();
                productDto.setId(cartItems.getProduct().getId());
                productDto.setName(cartItems.getProduct().getName());
                productDto.setPrice(cartItems.getPrice());
                productDto.setQuantity(cartItems.getQuantity());

                productDto.setByteImg(cartItems.getProduct().getImg());

                productDtoList.add(productDto);

            }
            orderedProductsResponseDto.setProductDtoList(productDtoList);
        }
        return orderedProductsResponseDto;
    }

    public ReviewDto giveReview(ReviewDto reviewDto) {
        Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());
    
        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
            Review review = new Review();
            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());
            review.setUser(optionalUser.get());
            review.setProduct(optionalProduct.get());
            
            // Save the review
            Review savedReview = reviewRepository.save(review);
    
            // Convert to ReviewDto and return it
            ReviewDto responseDto = savedReview.getDto();
    
            // Set the image data in the response DTO
            if (optionalProduct.get().getImg() != null) {
                responseDto.setReturnedImg(optionalProduct.get().getImg());
            }
    
            return responseDto;
        }
        return null;
    }
    
}