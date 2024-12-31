package br.com.judev.backend.mapper;

import br.com.judev.backend.dto.OrderDTO;
import br.com.judev.backend.dto.OrderItemDTO;
import br.com.judev.backend.model.Order;
import br.com.judev.backend.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
/**
 * Interface para mapeamento entre entidades e DTOs usando MapStruct.
 * O MapStruct é um framework que gera automaticamente implementações de mapeamento
 * entre objetos de diferentes tipos, como entidades e DTOs.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {
    /**
     * Converte uma entidade Order para um DTO OrderDTO.
     * - O campo `userId` no DTO será populado com o valor de `user.id` da entidade.
     * - O campo `orderItems` no DTO será populado com os itens da ordem (`items`).
     *
     * @param order a entidade Order.
     * @return o correspondente OrderDTO.
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "items")
    OrderDTO toDTO(Order order);

    /**
     * Converte um DTO OrderDTO para uma entidade Order.
     * - O campo `user.id` na entidade será populado com o valor de `userId` no DTO.
     * - O campo `items` na entidade será populado com os `orderItems` do DTO.
     *
     * @param orderDTO o DTO OrderDTO.
     * @return a entidade Order.
     */
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDTO orderDTO);

    /**
     * Converte uma lista de entidades Order para uma lista de DTOs OrderDTO.
     *
     * @param orders lista de entidades Order.
     * @return lista correspondente de OrderDTOs.
     */
    List<OrderDTO> toDTOs(List<Order> orders);

    /**
     * Converte uma lista de DTOs OrderDTO para uma lista de entidades Order.
     *
     * @param orderDTOS lista de OrderDTOs.
     * @return lista correspondente de entidades Order.
     */
    List<Order> toEntities(List<OrderDTO> orderDTOS);

    /**
     * Converte uma entidade OrderItem para um DTO OrderItemDTO.
     * - O campo `productId` no DTO será populado com o valor de `product.id` da entidade.
     *
     * @param orderItem a entidade OrderItem.
     * @return o correspondente OrderItemDTO.
     */
    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    /**
     * Converte um DTO OrderItemDTO para uma entidade OrderItem.
     * - O campo `product.id` na entidade será populado com o valor de `productId` no DTO.
     *
     * @param orderItemDTO o DTO OrderItemDTO.
     * @return a entidade OrderItem.
     */
    @Mapping(target = "product.id", source = "productId")
    OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO);

    /**
     * Converte uma lista de entidades OrderItem para uma lista de DTOs OrderItemDTO.
     *
     * @param orderItem lista de entidades OrderItem.
     * @return lista correspondente de OrderItemDTOs.
     */
    List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> orderItem);

    /**
     * Converte uma lista de DTOs OrderItemDTO para uma lista de entidades OrderItem.
     *
     * @param orderItemDTO lista de OrderItemDTOs.
     * @return lista correspondente de entidades OrderItem.
     */
    List<OrderItem> toOrderItemEntities(List<OrderItemDTO> orderItemDTO);
}
