package ru.yandex.practicum.commerce.dto.shopping.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageProductDto {

    List<ProductDto> content;
    Sort sort;
}
