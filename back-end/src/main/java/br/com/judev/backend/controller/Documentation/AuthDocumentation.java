package br.com.judev.backend.controller.Documentation;

import br.com.judev.backend.dto.requests.ChangePasswordRequest;
import br.com.judev.backend.dto.requests.EmailConfirmationRequest;
import br.com.judev.backend.dto.requests.LoginRequest;
import br.com.judev.backend.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface AuthDocumentation {

    @Operation(summary = "Login the user", description = "Authenticates the user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    String login(@Parameter(description = "The login request containing email and password") LoginRequest loginRequest);

    @Operation(summary = "Register a new user", description = "Registers a new user into the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    User register(@Parameter(description = "The user data to register") User user);

    @Operation(summary = "List all users", description = "Returns a list of all users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No users found")
    })
    Iterable<User> listAllUsers();

    @Operation(summary = "Change user password", description = "Allows a user to change their password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password data")
    })
    void changePassword(@Parameter(description = "The change password request with new password") ChangePasswordRequest request);

    @Operation(summary = "Confirm user email", description = "Confirms the user's email using a confirmation code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid confirmation code"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    void confirmEmail(@Parameter(description = "Email confirmation request with code") EmailConfirmationRequest request);

    @Operation(summary = "Get user role", description = "Retrieves the role of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    String getUserRole();

    @Operation(summary = "Get user email by ID", description = "Retrieves the email of a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User email retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    String getUserEmailById(@Parameter(description = "The user ID") Long id);
}
