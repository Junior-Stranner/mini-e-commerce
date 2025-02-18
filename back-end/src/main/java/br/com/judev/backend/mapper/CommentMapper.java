package br.com.judev.backend.mapper;


import br.com.judev.backend.dto.requests.CommentDTO;
import br.com.judev.backend.dto.responses.CommentReponseDTO;
import br.com.judev.backend.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "userId",source = "user.id")
    CommentReponseDTO toDTO(Comment comment);
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product", ignore = true)
    Comment toEntity(CommentDTO commentDTO);
}
