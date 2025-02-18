package br.com.judev.backend.mapper;

import br.com.judev.backend.dto.CommentDTO;
import br.com.judev.backend.dto.UserRequestDTO;
import br.com.judev.backend.dto.UserResponseDTO;
import br.com.judev.backend.model.Comment;
import br.com.judev.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "profilePicture", source = "profilePicture") // Exemplo de mapeamento direto
    UserResponseDTO toDTO(User user);

    @Mapping(target = "profilePicture", source = "profilePicture") // Exemplo de mapeamento direto
    User toEntity(UserRequestDTO userDTO);

    @Mapping(target = "userId", source = "user.id")
    CommentDTO toDTO(Comment comment);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product", ignore = true)
    Comment toEntity(CommentDTO commentDTO);
}
