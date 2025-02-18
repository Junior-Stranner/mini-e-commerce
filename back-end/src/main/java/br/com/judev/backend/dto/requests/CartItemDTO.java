package br.com.judev.backend.dto.requests;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long productId;
    @Positive
    private Integer quantity;
}
