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

    // Mapeamento de User para UserResponseDTO
    @Mapping(target = "role", source = "role") // Mapeia o papel (role) diretamente
    @Mapping(target = "emailConfirmation", source = "emailConfirmation") // Mapeia o status de confirmação de email
    @Mapping(target = "confirmationCode", source = "confirmationCode") // Mapeia o código de confirmação
    UserResponseDTO toDTO(User user);

    // Mapeamento de UserRequestDTO para User
    @Mapping(target = "role", source = "role") // Mapeia o papel (role) diretamente
    @Mapping(target = "emailConfirmation", ignore = true) // Ignora o emailConfirmation ao criar o usuário
    @Mapping(target = "confirmationCode", ignore = true) // Ignora o código de confirmação ao criar o usuário
    User toEntity(UserRequestDTO userRequestDTO);
}
