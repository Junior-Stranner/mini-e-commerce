package br.com.judev.backend.mapper;

import br.com.judev.backend.dto.CartDTO;
import br.com.judev.backend.dto.CartItemDTO;
import br.com.judev.backend.model.Cart;
import br.com.judev.backend.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    CartDTO toDTO(Cart Cart);

    @Mapping(target="user.id", source = "userId")
    Cart toEntity(CartDTO cartDTO);

    @Mapping(target="productId", source="product.id")
    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(target="product.id", source="productId")
    CartItem toEntity(CartItemDTO cartItemDTO);
}
