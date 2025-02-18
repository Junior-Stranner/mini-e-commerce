package br.com.judev.backend.services;

import br.com.judev.backend.dto.UserRequestDTO;
import br.com.judev.backend.dto.UserResponseDTO;
import br.com.judev.backend.dto.responses.ChangePasswordResponseDTO;
import br.com.judev.backend.exception.ResourceNotFoundException;
import br.com.judev.backend.dto.requests.ChangePasswordRequest;
import br.com.judev.backend.mapper.UserMapper;
import br.com.judev.backend.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.judev.backend.model.User;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    public UserResponseDTO registerUser(UserRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        User user = userMapper.toEntity(dto);

        String confirmationCode = generateConfirmationCode();

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(User.Role.USER);
        user.setConfirmationCode(confirmationCode);
        user.setEmailConfirmation(false);

        emailService.sendConfirmationCode(dto.getEmail(), confirmationCode);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }


    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    public ChangePasswordResponseDTO changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ChangePasswordResponseDTO("Password changed successfully");
    }


    public void confirmEmail(String email, String confirmationCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getConfirmationCode() == null || !user.getConfirmationCode().equals(confirmationCode)) {
            throw new BadCredentialsException("Invalid confirmation code");
        }
        user.setEmailConfirmation(true);
        user.setConfirmationCode(null);

        userRepository.save(user);
    }

    private String generateConfirmationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    //update
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    public List<UserResponseDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
