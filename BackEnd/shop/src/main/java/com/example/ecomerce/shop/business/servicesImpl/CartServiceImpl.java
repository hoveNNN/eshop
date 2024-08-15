package com.example.ecomerce.shop.business.servicesImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.ecomerce.shop.business.services.CartService;
import com.example.ecomerce.shop.dao.entity.CartItems;
import com.example.ecomerce.shop.dao.entity.Coupon;
import com.example.ecomerce.shop.dao.entity.Order;
import com.example.ecomerce.shop.dao.entity.Product;
import com.example.ecomerce.shop.dao.entity.User;
import com.example.ecomerce.shop.dao.enums.OrderStatus;
import com.example.ecomerce.shop.dao.repository.CartItemsRepository;
import com.example.ecomerce.shop.dao.repository.CouponRepository;
import com.example.ecomerce.shop.dao.repository.OrderRepository;
import com.example.ecomerce.shop.dao.repository.ProductRepository;
import com.example.ecomerce.shop.dao.repository.UserRepository;
import com.example.ecomerce.shop.exceptions.ValidationException;
import com.example.ecomerce.shop.web.dto.AddProductInCartDto;
import com.example.ecomerce.shop.web.dto.CartItemsDto;
import com.example.ecomerce.shop.web.dto.OrderDto;
import com.example.ecomerce.shop.web.dto.PlaceOrderDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    CouponRepository couponRepository;

    @Override
    public ResponseEntity<?> addProductInCart(AddProductInCartDto addProductInCartDto) {
        log.debug("Adding product to cart: {}", addProductInCartDto);

        // Validate User
        Optional<User> userOpt = userRepository.findById(addProductInCartDto.getUserId());
        if (userOpt.isEmpty()) {
            log.error("User not found: {}", addProductInCartDto.getUserId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        User user = userOpt.get();

        // Validate Product
        Optional<Product> productOpt = productRepository.findById(addProductInCartDto.getProductId());
        if (productOpt.isEmpty()) {
            log.error("Product not found: {}", addProductInCartDto.getProductId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        Product product = productOpt.get();

        // Find or create active order
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(user.getId(), OrderStatus.Pending);
        if (activeOrder == null) {
            activeOrder = new Order();
            activeOrder.setUser(user);
            activeOrder.setOrderStatus(OrderStatus.Pending);
            activeOrder.setTotalAmount(0L);
            activeOrder.setAmount(0L);
            activeOrder.setTrackingId(UUID.randomUUID());
            activeOrder = orderRepository.save(activeOrder); // Save the new order to get a valid ID
            log.debug("New order created: {}", activeOrder);
        }

        // Ensure the order ID is valid
        if (activeOrder.getId() == null) {
            log.error("Order ID is null after saving: {}", activeOrder);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order ID is null after saving.");
        }

        // Check if the product is already in the cart
        Optional<CartItems> cartItemsOpt = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                product.getId(), activeOrder.getId(), user.getId());

        if (cartItemsOpt.isPresent()) {
            log.warn("Product already in cart: {}", product.getId());
            // Update the existing cart item
            CartItems existingCartItem = cartItemsOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            existingCartItem.setPrice(existingCartItem.getPrice() + product.getPrice());
            CartItems updatedCartItem = cartItemsRepository.save(existingCartItem);

            // Update order amounts
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());
            activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
            orderRepository.save(activeOrder);

            log.debug("Updated existing cart item: {}", updatedCartItem);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCartItem);
        } else {
            // Add product to cart
            CartItems cart = new CartItems();
            cart.setProduct(product);
            cart.setPrice(product.getPrice());
            cart.setQuantity(1L);
            cart.setUser(user);
            cart.setOrder(activeOrder);

            CartItems updatedCart = cartItemsRepository.save(cart);

            // Update order amounts
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
            activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
            orderRepository.save(activeOrder);

            log.debug("Product added to cart: {}", updatedCart);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
        }
    }

    public OrderDto getCartByUserId(Long userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        List<CartItemsDto> cartItemsDtoList = activeOrder.getCartItems().stream().map(CartItems::getCartDto)
                .collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDisount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setCartItems(cartItemsDtoList);
        if (activeOrder.getCoupon() != null) {
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }

        return orderDto;
    }

    public OrderDto applyCoupon(long userId, String code) {

        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException("Coupon not found"));
        if (couponIsExpired(coupon)) {
            throw new ValidationException("Coupon has expired");
        }
        double discountAmount = ((coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount());
        double netAmount = activeOrder.getTotalAmount() - discountAmount;
        activeOrder.setAmount((long) netAmount);
        activeOrder.setDiscount((long) discountAmount);
        activeOrder.setCoupon(coupon);
        orderRepository.save(activeOrder);
        return activeOrder.getOrderDto();
    }

    private boolean couponIsExpired(Coupon coupon) {
        Date currenDate = new Date();
        Date expirationDate = coupon.getExpirationDate();
        return expirationDate != null && currenDate.after(expirationDate);
    }

    public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(),
                OrderStatus.Pending);
        Optional<Product> product = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> cartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
        if (product.isPresent() && cartItem.isPresent()) {
            CartItems item = cartItem.get();
            Product prod = product.get();

            activeOrder.setAmount(activeOrder.getAmount() + prod.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + prod.getPrice());
            item.setQuantity(item.getQuantity() + 1);
            if (activeOrder.getCoupon() != null) {
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());
                double netAmount = activeOrder.getTotalAmount() - discountAmount;
                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);

            }

            cartItemsRepository.save(item);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();

        }
        return null;
    }
    public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(),
                OrderStatus.Pending);
        Optional<Product> product = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> cartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
        if (product.isPresent() && cartItem.isPresent()) {
            CartItems item = cartItem.get();
            Product prod = product.get();

            activeOrder.setAmount(activeOrder.getAmount() - prod.getPrice());
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - prod.getPrice());
            item.setQuantity(item.getQuantity() - 1);
            if (activeOrder.getCoupon() != null) {
                double discountAmount = ((activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount());
                double netAmount = activeOrder.getTotalAmount() - discountAmount;
                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);

            }

            cartItemsRepository.save(item);
            orderRepository.save(activeOrder);
            return activeOrder.getOrderDto();

        }
        return null;
    }

    public OrderDto placeOrder(PlaceOrderDto placeOrderDto){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(),OrderStatus.Pending);
        Optional<User> user=userRepository.findById(placeOrderDto.getUserId());
        if(user.isPresent()){
            
            activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
            activeOrder.setAddress(placeOrderDto.getAddress());
            activeOrder.setDate(new Date());
            activeOrder.setOrderStatus(OrderStatus.Placed);
            activeOrder.setTrackingId(UUID.randomUUID());
            orderRepository.save(activeOrder);

            Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(user.get());
        order.setOrderStatus(OrderStatus.Pending);
        orderRepository.save(order);

        return activeOrder.getOrderDto();


        }
        return null;
    }   

    public List<OrderDto>getMyPlaceOrders(Long userId){
        return orderRepository.findByUserIdAndOrderStatusIn(userId,List.of(OrderStatus.Placed,OrderStatus.Shipped,OrderStatus.Delivered)).stream().map(Order::getOrderDto).collect(Collectors.toList());
        
    }
}
