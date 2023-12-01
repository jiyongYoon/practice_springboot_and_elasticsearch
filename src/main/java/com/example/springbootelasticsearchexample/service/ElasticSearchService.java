package com.example.springbootelasticsearchexample.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.springbootelasticsearchexample.entity.Product;
import com.example.springbootelasticsearchexample.util.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public SearchResponse<Map> matchAllServices() throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplier();
        SearchResponse<Map> searchResponse =
                elasticsearchClient.search(s -> s.query(supplier.get()), Map.class);
        return searchResponse;
    }

    public SearchResponse<Product> matchAllProductsServices() throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplier();
        SearchResponse<Product> searchResponse =
                elasticsearchClient.search(s -> s.index("products").query(supplier.get()), Product.class);
        return searchResponse;
    }

    public SearchResponse<Product> matchProductsWithName(String fieldValue) throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.supplierWithNameField(fieldValue);
        SearchResponse<Product> searchResponse =
                elasticsearchClient.search(s -> s.index("products").query(supplier.get()), Product.class);
        return searchResponse;
    }

    public SearchResponse<Product> boolQueryWithNameAndPrice(String fieldValue, Integer lt, Integer gt) throws IOException {
        Supplier<Query> supplier = ElasticSearchUtil.boolQuerySupplier(fieldValue, lt, gt);
        SearchResponse<Product> searchResponse =
                elasticsearchClient.search(s -> s.index("products").query(supplier.get()), Product.class);
        return searchResponse;
    }
}
