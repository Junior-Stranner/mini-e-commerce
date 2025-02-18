package br.com.judev.backend.controller.Documentation;

import br.com.judev.backend.dto.requests.CommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface CommentDocumentation {

    @Operation(summary = "Add a comment to a product", description = "Allows an authenticated user to add a comment to a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    CommentDTO addComment(
            @Parameter(description = "Product ID to add the comment to") Long productId,
            @Parameter(description = "Authenticated user details") Long userId,
            @Parameter(description = "Comment data") CommentDTO commentDTO
    );

    @Operation(summary = "Get all comments for a product", description = "Retrieves all comments for a given product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    List<CommentDTO> getCommentsByProduct(
            @Parameter(description = "Product ID to get comments for") Long productId
    );
}
