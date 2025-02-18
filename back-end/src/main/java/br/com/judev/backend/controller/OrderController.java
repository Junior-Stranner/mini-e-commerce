package br.com.judev.backend.controller;

import br.com.judev.backend.dto.requests.OrderDTO;
import br.com.judev.backend.dto.responses.OrderResponseDTO;
import br.com.judev.backend.model.Order;
import br.com.judev.backend.model.User;
import br.com.judev.backend.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestParam String address,
                                                        @RequestParam String phoneNumber){
        Long userId = ((User) userDetails).getId();
        OrderResponseDTO orderDTO = orderService.createOrder(userId, address, phoneNumber);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = ((User) userDetails).getId();
        List<OrderResponseDTO> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId,
                                                      @RequestParam Order.OrderStatus status){
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
