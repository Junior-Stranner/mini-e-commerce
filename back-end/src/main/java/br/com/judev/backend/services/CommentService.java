package br.com.judev.backend.services;

import br.com.judev.backend.dto.requests.CommentDTO;
import br.com.judev.backend.dto.responses.CommentReponseDTO;
import br.com.judev.backend.exception.ResourceNotFoundException;
import br.com.judev.backend.mapper.CommentMapper;
import br.com.judev.backend.model.Comment;
import br.com.judev.backend.model.Product;
import br.com.judev.backend.model.User;
import br.com.judev.backend.repositories.CommentRepository;
import br.com.judev.backend.repositories.ProductRepository;
import br.com.judev.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentReponseDTO addComment(Long productId, Long userId, CommentDTO commentDTO){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setProduct(product);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    public List<CommentReponseDTO> getCommentsByProduct(Long productId){
        List<Comment> comments = commentRepository.findByProductId(productId);
        return comments.stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }

}
