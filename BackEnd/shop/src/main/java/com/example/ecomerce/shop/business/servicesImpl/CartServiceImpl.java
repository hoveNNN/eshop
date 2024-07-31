package com.example.ecomerce.shop.business.servicesImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.CartService;
import com.example.ecomerce.shop.dao.entity.CartItems;
import com.example.ecomerce.shop.dao.entity.Order;
import com.example.ecomerce.shop.dao.entity.Product;
import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.enums.OrderStatus;
import com.example.ecomerce.shop.dao.repository.CartItemsRepository;
import com.example.ecomerce.shop.dao.repository.OrderRepository;
import com.example.ecomerce.shop.dao.repository.ProductRepository;
import com.example.ecomerce.shop.dao.repository.UserRepository;
import com.example.ecomerce.shop.web.dto.AddProductInCartDto;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> addProductInCart(AddProductInCartDto addProductInCartDto) {
        Order activOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        Optional<CartItems> cartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activOrder.getId(), addProductInCartDto.getUserId());
            if(cartItems.isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }else {
                Optional<Product> product=productRepository.findById(addProductInCartDto.getProductId());
                Optional<User> user=userRepository.findById(addProductInCartDto.getUserId());
                if(product.isPresent()&& user.isPresent()){
                    CartItems cart = new CartItems();
                    cart.setProduct(product.get());
                    cart.setPrice(product.get().getPrice());
                    cart.setQuantity(1L);
                    cart.setUser(user.get());
                    cart.setOrder(activOrder);

                    CartItems updateCart=cartItemsRepository.save(cart);

                    activOrder.setTotalAmount(activOrder.getTotalAmount()+ cart.getPrice());
                    activOrder.setAmount(activOrder.getAmount()+ cart.getPrice());
                    activOrder.getCartItems().add(cart);
                    orderRepository.save(activOrder);
                    return ResponseEntity.status(HttpStatus.CREATED).body(cart);


                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product not found");
                }
            }
    }
}