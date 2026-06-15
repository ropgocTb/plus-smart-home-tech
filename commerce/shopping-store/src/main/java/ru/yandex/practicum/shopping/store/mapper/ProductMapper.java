package ru.yandex.practicum.shopping.store.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.shopping.store.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto mapToDto(Product product);

    Product mapToProduct(ProductDto productDto);
}
