package br.com.judev.backend.services;

import br.com.judev.backend.dto.CartDTO;
import br.com.judev.backend.exception.InsufficientStockException;
import br.com.judev.backend.exception.ResourceNotFoundException;
import br.com.judev.backend.mapper.CartMapper;
import br.com.judev.backend.model.Cart;
import br.com.judev.backend.model.CartItem;
import br.com.judev.backend.model.Product;
import br.com.judev.backend.model.User;
import br.com.judev.backend.repositories.CartRepository;
import br.com.judev.backend.repositories.ProductRepository;
import br.com.judev.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartDTO addToCart(Long userId, Long productId, Integer quantity){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product not found"));

        if(product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough available");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(null, user, new ArrayList<>()));

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem(null, cart, product, quantity);
            cart.getItems().add(cartItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDTO(savedCart);
        }

    public CartDTO getCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found"));

        return cartMapper.toDTO(cart);
    }

    public void clearCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public void removeCartItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cartRepository.save(cart);
    }
}
