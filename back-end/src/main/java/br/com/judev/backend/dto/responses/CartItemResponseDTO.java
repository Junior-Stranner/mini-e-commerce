package br.com.judev.backend.dto.responses;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemResponseDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
}
