package br.com.judev.backend.mapper;


import br.com.judev.backend.dto.requests.CommentDTO;
import br.com.judev.backend.dto.requests.ProductDTO;
import br.com.judev.backend.dto.responses.ProductResponseDTO;
import br.com.judev.backend.model.Comment;
import br.com.judev.backend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "image", source = "image") //add mappin
    ProductResponseDTO toDTO(Product product);

    @Mapping(target = "image", source = "image") //add mapping
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "userId",source = "user.id")
    CommentDTO toDTO(Comment comment);
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product", ignore = true)
    Comment toEntity(CommentDTO commentDTO);
}
