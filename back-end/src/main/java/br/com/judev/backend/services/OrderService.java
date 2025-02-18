package br.com.judev.backend.services;

import br.com.judev.backend.dto.requests.CartDTO;
import br.com.judev.backend.dto.requests.OrderDTO;
import br.com.judev.backend.dto.responses.CartResponseDTO;
import br.com.judev.backend.dto.responses.OrderResponseDTO;
import br.com.judev.backend.exception.InsufficientStockException;
import br.com.judev.backend.exception.ResourceNotFoundException;
import br.com.judev.backend.mapper.CartMapper;
import br.com.judev.backend.mapper.OrderMapper;
import br.com.judev.backend.model.*;
import br.com.judev.backend.repositories.OrderRepository;
import br.com.judev.backend.repositories.ProductRepository;
import br.com.judev.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

    @Transactional
    public OrderResponseDTO createOrder(Long userId, String address, String phoneNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.isEmailConfirmation()) {
            throw new IllegalStateException("Email not confirmed. Please confirm email before placing order");
        }
        CartResponseDTO cartDTO = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDTO);

        if(cart.getItems().isEmpty()){
            throw new IllegalStateException("Cannot create an order with an empty cart");
        }
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        try{
            emailService.sendOrderConfirmation(savedOrder);
        }catch (MailException e){
            logger.error("Failed to send order confirmation email for order ID "+savedOrder.getId(), e);
        }
        return orderMapper.toDTO(savedOrder);
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order){
        return cart.getItems().stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(()-> new EntityNotFoundException("Product not found with id: "+cartItem.getProduct().getId()));

            if (product.getQuantity() == null || product.getQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product " + product.getName());
            }
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return new OrderItem(null, order, product, cartItem.getQuantity(), product.getPrice());
        }).collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getAllOrders(){
       List <Order> orders = orderRepository.findAll();
    return orders.stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getUserOrders(Long userId){
        return orderMapper.toDTOs(orderRepository.findByUserId(userId));
    }

    public OrderResponseDTO updateOrderStatus(Long orderId,Order.OrderStatus status){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }

}
