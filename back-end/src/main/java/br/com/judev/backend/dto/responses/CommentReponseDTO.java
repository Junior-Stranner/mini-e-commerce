package br.com.judev.backend.dto.responses;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentReponseDTO {
    private Long id;
    private String content;
    private Integer score;
    private Long userId;
}
