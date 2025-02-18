package br.com.judev.backend.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long userId; //usedId
    private List<CartItemDTO> items;
}
