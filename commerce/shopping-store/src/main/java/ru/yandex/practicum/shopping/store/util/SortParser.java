package ru.yandex.practicum.shopping.store.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortParser {

    // по умолчанию - возрастающий id
    // аргументы - [{свойство, порядок}, ...]
    public Sort parseOrder(String[] args) {
        if (args == null || args.length == 0) {
            return Sort.by(Sort.Direction.ASC, "productId");
        }

        Sort order = Sort.unsorted();

        for (String s : args) {
            String[] element = s.split(",");
            String property = element[0].trim();
            Sort.Direction direction = Sort.Direction.ASC;

            if (element.length == 2 && element[1].trim().equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }

            order = order.and(Sort.by(direction, property));
        }

        return order;
    }
}
