package br.com.judev.backend.dto;

import br.com.judev.backend.model.Cart;
import br.com.judev.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor@NoArgsConstructor
@Getter@Setter
public class UserResponseDTO {

    private Long id;
    private String email;
    private String password;
    private User.Role role;
    private boolean emailConfirmation;
    private String confirmationCode;
}
