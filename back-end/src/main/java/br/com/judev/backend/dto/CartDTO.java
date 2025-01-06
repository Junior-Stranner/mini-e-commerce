package br.com.judev.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private Long userId; //usedId
    private List<com.example.demo.dto.CartItemDTO> items;
}
