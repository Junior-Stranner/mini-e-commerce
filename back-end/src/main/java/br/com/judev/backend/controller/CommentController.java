package br.com.judev.backend.controller;

import br.com.judev.backend.dto.requests.CommentDTO;
import br.com.judev.backend.dto.responses.CommentReponseDTO;
import br.com.judev.backend.model.User;
import br.com.judev.backend.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/product/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentReponseDTO> addComment(@PathVariable Long productId,
                                                        @AuthenticationPrincipal UserDetails userDetails,
                                                        @Valid @RequestBody CommentDTO commentDTO){
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(commentService.addComment(productId, userId, commentDTO));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentReponseDTO>> getCommentsByProduct(@PathVariable Long productId){
        return ResponseEntity.ok(commentService.getCommentsByProduct(productId));
    }
}

/* @AuthenticationPrincipal UserDetails userDetails

Essa anotação é usada para injetar o principal autenticado (o usuário atual) no método.
O objeto UserDetails contém informações do usuário autenticado, como nome de usuário, credenciais e permissões.

Quando um usuário é autenticado no Spring Security, o objeto correspondente ao usuário (geralmente implementa UserDetails) é armazenado no contexto de segurança (SecurityContext).
Usando @AuthenticationPrincipal, você pode obter diretamente o principal associado à requisição atual.*/

/*Use @PreAuthorize("isAuthenticated()") para proteger endpoints de acesso não autenticado.
 Apenas usuários logados poderão acessá-los.
 */