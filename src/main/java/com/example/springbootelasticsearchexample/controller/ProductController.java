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

        return getListOfHitsFromSearchResponse(searchResponse);
    }

    @GetMapping("/matchAllProducts/{fieldValue}")
    public List<Product> matchAllProductsWithName(@PathVariable String fieldValue) throws IOException {
        SearchResponse<Product> searchResponse = elasticSearchService.matchProductsWithName(fieldValue);

        return getListOfHitsFromSearchResponse(searchResponse);
    }

    @GetMapping("/bool")
    public List<Product> boolWithNameAndPriceRange(@RequestParam String fieldValue,
                                                   @RequestParam Integer lt,
                                                   @RequestParam Integer gt) throws IOException {
        SearchResponse<Product> searchResponse =
                elasticSearchService.boolQueryWithNameAndPrice(fieldValue, lt, gt);

        return getListOfHitsFromSearchResponse(searchResponse);
    }

    private static <T> List<T> getListOfHitsFromSearchResponse(SearchResponse<T> searchResponse) {
        List<Hit<T>> listOfHits = searchResponse.hits().hits();
        List<T> listOfProducts = new ArrayList<>();
        for (Hit<T> listOfHit : listOfHits) {
            listOfProducts.add(listOfHit.source());
        }
        System.out.println(listOfProducts);
        return listOfProducts;
    }

}
