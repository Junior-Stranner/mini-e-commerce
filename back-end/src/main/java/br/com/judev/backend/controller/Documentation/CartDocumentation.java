package br.com.judev.backend.controller.Documentation;

import br.com.judev.backend.dto.requests.CartItemDTO;
import br.com.judev.backend.model.Cart;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface CartDocumentation {

    @Operation(summary = "Add item to the cart", description = "Adds a new item to the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid item data")
    })
    Cart addItemToCart(@Parameter(description = "The item to add to the cart") CartItemDTO cartItemRequest);

    @Operation(summary = "Remove item from the cart", description = "Removes an item from the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    Cart removeItemFromCart(@Parameter(description = "The ID of the item to remove from the cart") Long itemId);

    @Operation(summary = "Get all items in the cart", description = "Retrieves all items currently in the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of items in the cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart is empty")
    })
    Cart getAllItemsInCart();

    @Operation(summary = "Clear the cart", description = "Empties all items from the user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    void clearCart();
}
