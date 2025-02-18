package br.com.judev.backend.dto.responses;

import br.com.judev.backend.dto.requests.CartItemDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartResponseDTO {
    private Long id;
    private Long userId; //usedId
    private List<CartItemDTO> items;
}
