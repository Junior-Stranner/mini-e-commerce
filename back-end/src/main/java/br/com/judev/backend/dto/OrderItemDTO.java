package br.com.judev.backend.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long id;
    private Long productId;
    @Positive
    private Integer quantity;
    @Positive
    private BigDecimal price;
}
