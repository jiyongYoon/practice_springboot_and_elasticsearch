package com.example.springbootelasticsearchexample.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ElasticSearchUtilTest {

    @Test
    void rangeQueryWithPrice() {
        RangeQuery rangeQuery = ElasticSearchUtil.rangeQueryWithPrice(10, 50);
        Supplier<Query> supplier = () -> Query.of(q -> q.range(rangeQuery));
        System.out.println(rangeQuery);
        System.out.println(supplier.get().toString());
    }

    @Test
    void boolQuery() {
        Supplier<Query> supplier = ElasticSearchUtil.boolQuerySupplier("charger", 5000, 1000);
        System.out.println(supplier.get().toString());
    }
}