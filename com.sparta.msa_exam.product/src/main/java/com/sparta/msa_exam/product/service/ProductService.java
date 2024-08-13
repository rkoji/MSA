package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(cacheNames = "product_cahce")
    public List<ProductResponseDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertTonDto)
                .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "product_cahce",allEntries = true)
    public ProductResponseDto addProduct(ProductRequestDto dto) {
        Product product = new Product();
        product.saveProduct(dto);

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDto(
                savedProduct.getProduct_id(),
                savedProduct.getName(),
                savedProduct.getSupply_price()
        );
    }

    public Long getProduct(Long productId) {
        // product_id 가 상품 목록에 존재하는지 검증
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 상품입니다."));
        return product.getProduct_id();
    }

    private ProductResponseDto convertTonDto(Product product) {
        return new ProductResponseDto(
                product.getProduct_id(),
                product.getName(),
                product.getSupply_price()
        );
    }
}
