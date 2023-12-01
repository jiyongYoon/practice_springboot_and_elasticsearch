package com.example.springbootelasticsearchexample.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.springbootelasticsearchexample.entity.Product;
import com.example.springbootelasticsearchexample.service.ElasticSearchService;
import com.example.springbootelasticsearchexample.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    private final ElasticSearchService elasticSearchService;

    @GetMapping("/findAll")
    public Iterable<Product> findAll() {
        return productService.getProductList();
    }


    @PostMapping("/insert")
    public Product insertProduct(@RequestBody Product product) {
        return productService.insertProduct(product);
    }

    @GetMapping("/matchAll")
    public SearchResponse<Map> matchAll() throws IOException {
        SearchResponse<Map> searchResponse = elasticSearchService.matchAllServices();
        System.out.println(searchResponse.hits().hits().toString());
        return searchResponse;
    }

    @GetMapping("/matchAllProducts")
    public List<Product> matchAllProducts() throws IOException {
        SearchResponse<Product> searchResponse = elasticSearchService.matchAllProductsServices();

        List<Hit<Product>> listOfHits = searchResponse.hits().hits();
        List<Product> listOfProducts = new ArrayList<>();
        for (Hit<Product> listOfHit : listOfHits) {
            listOfProducts.add(listOfHit.source());
        }
        System.out.println(listOfProducts);
        /*
        [
            Product(id=1, name=mobile, description=good phone, quantity=1, price=200000.0),
            Product(id=2, name=laptop, description=good laptop, quantity=1, price=600000.0),
            Product(id=3, name=charger, description=good charger, quantity=1, price=1000.0)
        ]
        */
        return listOfProducts;
    }

    @GetMapping("/matchAllProducts/{fieldValue}")
    public List<Product> matchAllProductsWithName(@PathVariable String fieldValue) throws IOException {
        SearchResponse<Product> searchResponse = elasticSearchService.matchProductsWithName(fieldValue);

        List<Hit<Product>> listOfHits = searchResponse.hits().hits();
        List<Product> listOfProducts = new ArrayList<>();
        for (Hit<Product> listOfHit : listOfHits) {
            listOfProducts.add(listOfHit.source());
        }
        System.out.println(listOfProducts);
        return listOfProducts;
    }

}
