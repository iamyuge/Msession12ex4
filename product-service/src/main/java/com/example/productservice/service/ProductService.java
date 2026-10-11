package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {

        System.out.println(
                "Đang truy vấn PostgreSQL cho productId = " + id
        );

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy sản phẩm ID = " + id
                        )
                );
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, Product product) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy sản phẩm ID = " + id
                        )
                );

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setPrice(product.getPrice());

        System.out.println(
                "Đã cập nhật sản phẩm ID = " + id
                        + ", cache sẽ bị xóa"
        );

        return productRepository.save(existingProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy sản phẩm ID = " + id
                        )
                );

        productRepository.delete(product);
    }
}