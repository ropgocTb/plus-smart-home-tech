package ru.yandex.practicum.shopping.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.shopping.store.PageProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductState;
import ru.yandex.practicum.commerce.exception.shopping.store.ProductNotFoundException;
import ru.yandex.practicum.commerce.request.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.shopping.store.mapper.ProductMapper;
import ru.yandex.practicum.shopping.store.model.Product;
import ru.yandex.practicum.shopping.store.repository.ProductRepository;
import ru.yandex.practicum.shopping.store.util.SortParser;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final SortParser sortParser;

    @Override
    public PageProductDto getProductsByCategory(ProductCategory category, int page, int size, String[] sort) {
        log.info("Get products by category: {}; page: {}; size: {}, sort: {}", category, page, size, sort);

        Sort order = sortParser.parseOrder(sort);

        List<Product> productList = repository.findByProductCategory(category, PageRequest.of(page, size, order));

        return new PageProductDto(productList.stream()
                .map(mapper::mapToDto)
                .toList(), order);
    }

    @Override
    @Transactional(readOnly = false)
    public ProductDto addProduct(ProductDto productDto) {
        log.info("Add product: {}", productDto);

        return mapper.mapToDto(repository.save(mapper.mapToProduct(productDto)));
    }

    @Override
    @Transactional(readOnly = false)
    public ProductDto updateProduct(ProductDto productDto) {
        log.info("Update product: {}", productDto);

        Product product = mapper.mapToProduct(productDto);

        if (!repository.existsById(product.getProductId())) {
            throw new ProductNotFoundException("Cannot update: product id not found");
        }

        return mapper.mapToDto(repository.save(product));
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean removeProduct(UUID productId) {
        log.info("Remove product: {}", productId);

        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Cannot delete: product not found"));

        if (product.getProductState().equals(ProductState.ACTIVE)) {
            product.setProductState(ProductState.DEACTIVATE);
            log.info("Deactivated product: {}", productId);
            return true;
        }

        log.info("Product already deactivated: {}", productId);
        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean setProductQuantity(SetProductQuantityStateRequest stateRequest) {
        log.info("SetProductQuantity: {}", stateRequest);
        Product product = repository.findById(stateRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Cannot set quantity: product not found"));
        product.setQuantityState(stateRequest.getQuantityState());
        return true;
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        log.info("Get product: {}", productId);

        return mapper.mapToDto(repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found")));
    }
}
