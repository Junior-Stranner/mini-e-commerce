package br.com.judev.backend.controller.Documentation;

import br.com.judev.backend.dto.requests.OrderDTO;
import br.com.judev.backend.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface OrderDocumentation {

    @Operation(summary = "Create an order", description = "Allows an authenticated user to create an order with address and phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    OrderDTO createOrder(
            @Parameter(description = "Authenticated user details") Long userId,
            @Parameter(description = "Address of the user") String address,
            @Parameter(description = "Phone number of the user") String phoneNumber
    );

    @Operation(summary = "Get all orders", description = "Retrieve all orders, accessible only by ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    List<OrderDTO> getAllOrders();

    @Operation(summary = "Get orders by user", description = "Retrieve orders by the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    List<OrderDTO> getUserOrders(
            @Parameter(description = "Authenticated user details") Long userId
    );

    @Operation(summary = "Update order status", description = "Allows an ADMIN to update the status of an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    OrderDTO updateOrderStatus(
            @Parameter(description = "ID of the order to update") Long orderId,
            @Parameter(description = "New status of the order") Order.OrderStatus status
    );
}
