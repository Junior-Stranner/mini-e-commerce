package br.com.judev.backend.dto.responses;

import br.com.judev.backend.dto.requests.OrderItemDTO;
import br.com.judev.backend.model.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private String address;
    private String phoneNumber;
    private Order.OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> orderItems;
}
