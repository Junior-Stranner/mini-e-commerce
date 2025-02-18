package br.com.judev.backend.controller;

import br.com.judev.backend.dto.requests.CartDTO;
import br.com.judev.backend.dto.responses.CartResponseDTO;
import br.com.judev.backend.model.User;
import br.com.judev.backend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;


    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity){
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = ((User) userDetails).getId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    //update
    @DeleteMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long productId) {
        Long userId = ((User) userDetails).getId();
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
