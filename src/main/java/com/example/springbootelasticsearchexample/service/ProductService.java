package com.example.springbootelasticsearchexample.service;

import com.example.springbootelasticsearchexample.entity.Product;
import com.example.springbootelasticsearchexample.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Iterable<Product> getProductList() {
        return productRepository.findAll();
    }

    public Product insertProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, Integer id) {
        Product findProduct = productRepository.findById(id).orElseThrow();
        findProduct.setPrice(product.getPrice());
        return findProduct;
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
