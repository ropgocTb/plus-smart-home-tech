package ru.yandex.practicum.shopping.cart.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    ShoppingCartDto mapToDto(ShoppingCart cart);

    ShoppingCart mapToCart(ShoppingCartDto cartDto);
}
