package br.com.judev.backend.controller;

import br.com.judev.backend.dto.*;
import br.com.judev.backend.dto.requests.ChangePasswordRequest;
import br.com.judev.backend.dto.requests.EmailConfirmationRequest;
import br.com.judev.backend.dto.requests.LoginRequest;
import br.com.judev.backend.exception.ResourceNotFoundException;
import br.com.judev.backend.services.JwtService;
import br.com.judev.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        final UserDetails userDetails = (UserDetails) userService.getUserByEmail(loginRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO dto){
        UserResponseDTO responseDTO = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public List<UserResponseDTO> ListAllUsers(){
        return userService.findAllUsers();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.changePassword(email, request);
        return ResponseEntity.ok().body("Password changed");
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailConfirmationRequest request){
        try{
            userService.confirmEmail(request.getEmail(), request.getConfirmationCode());
            return ResponseEntity.ok().body("Email confirmed successfuly");
        }catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body("Invalid confirmation code");
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    //update
    @GetMapping("/user/role")
    public ResponseEntity<String> getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserResponseDTO user = userService.getUserByEmail(email);

        if (user != null) {
            String role = String.valueOf(user.getRole());
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    //update
    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserEmailById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id); // Ensure this method exists in your UserService
        if (user != null) {
            return ResponseEntity.ok(user.getEmail());
        }
        return ResponseEntity.notFound().build();
    }
}

