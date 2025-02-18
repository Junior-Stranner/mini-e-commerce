package br.com.judev.backend.mapper;


import br.com.judev.backend.dto.responses.CartResponseDTO;
import br.com.judev.backend.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    CartResponseDTO toDTO(Cart Cart);
    @Mapping(target="user.id", source = "userId")
    Cart toEntity(CartResponseDTO cartDTO);

 /*   @Mapping(target="productId", source="product.id")
    CartResponseDTO toDTO(CartItem cartItem);

    @Mapping(target="product.id", source="productId")
    CartItem toEntity(CartItemDTO cartItemDTO);*/
}
