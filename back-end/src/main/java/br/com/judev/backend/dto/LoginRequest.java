package br.com.judev.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }
}
