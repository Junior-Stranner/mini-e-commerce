package br.com.judev.backend.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Integer scroe;
    private Long iserId;
}
