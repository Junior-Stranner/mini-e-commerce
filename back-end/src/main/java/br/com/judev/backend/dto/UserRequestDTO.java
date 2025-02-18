package br.com.judev.backend.dto;

import br.com.judev.backend.model.Cart;
import br.com.judev.backend.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class UserRequestDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank(message = "Password cannot be empty or just be wihgt spaces")
    @Size(min = 6)
    private String password;
    @Enumerated(EnumType.STRING)
    private User.Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
    private boolean emailConfirmation;
    private String confirmationCode;


}
