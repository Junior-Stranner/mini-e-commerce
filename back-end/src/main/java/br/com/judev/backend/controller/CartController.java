package br.com.judev.backend.controller;

import br.com.judev.backend.dto.CartDTO;
import br.com.judev.backend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;


    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestParam Long productId,
                                             @RequestParam Integer quantity){
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
