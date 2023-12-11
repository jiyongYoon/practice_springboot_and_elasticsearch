package com.example.springbootelasticsearchexample.service;

import com.example.springbootelasticsearchexample.entity.Category;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 'org.springframework.data:spring-data-elasticsearch:4.4.14'의 하위 의존성에 'org.elasticsearch:elasticsearch` 에 있는 QueryBuilder를 사용하는 방법 <br>
 * 이 방법이 더 직관적이고 BoolQuery를 조합하는 것이 편하다고 판단됨.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ElasticSearchServiceBySpringDataElasticsearch {

    private final ElasticsearchOperations operations;

    public List<Category> basicMatchQuery() {
        QueryBuilder matchQuery =
                QueryBuilders.matchQuery("fieldName", "searchValue1 searchValue2")
                                .operator(Operator.OR); // operator 추가 가능

        return getDocumentList(matchQuery, Category.class);
    }

    public List<Category> basicBoolQuery() {
        QueryBuilder matchQuery = QueryBuilders.matchQuery("fieldName", "searchValue");

        QueryBuilder notMatchQuery = QueryBuilders.matchQuery("fieldName", "excludeSearchValue");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(matchQuery)
                .mustNot(notMatchQuery)
                .should(null) // should, filter도 추가 가능
                .filter(null);

        return getDocumentList(boolQueryBuilder, Category.class);
    }

    public List<Category> basicNestedQuery() {
        QueryBuilder matchQuery = QueryBuilders.matchQuery("nestedFieldName", "searchValue");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(matchQuery);

        NestedQueryBuilder nestedQueryBuilder =
                QueryBuilders.nestedQuery("fieldPath", boolQueryBuilder, ScoreMode.Total);

        return getDocumentList(nestedQueryBuilder, Category.class);
    }

    public <TDocument> List<TDocument> getDocumentList(QueryBuilder queryBuilder,
                                                       Class<TDocument> tDocumentClass) {

        final NativeSearchQueryBuilder nativeSearchQueryBuilder = searchQuery(queryBuilder);
        Sort sort = Sort.by(Sort.Order.desc("fieldName"));
        NativeSearchQuery searchQuery =
                nativeSearchQueryBuilder.withSort(sort).build().setPageable(PageRequest.of(0, 100));

        System.out.println("searchQuery: " + searchQuery);
        SearchHits<TDocument> documentSearchHits = operations.search(searchQuery, tDocumentClass);

        return documentSearchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private <TDocument> List<TDocument> getDocumentList(QueryBuilder queryBuilder,
                                                        Class<TDocument> tDocumentClass,
                                                        Pageable pageable) {

        final NativeSearchQueryBuilder nativeSearchQueryBuilder = searchQuery(queryBuilder);
        Sort sort = Sort.by(Sort.Order.desc("fieldName"));
        NativeSearchQuery searchQuery =
                nativeSearchQueryBuilder.withSort(sort).build().setPageable(
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

        System.out.println("searchQuery: " + searchQuery);
        SearchHits<TDocument> documentSearchHits = operations.search(searchQuery, tDocumentClass);

        return documentSearchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    private NativeSearchQueryBuilder searchQuery(QueryBuilder queryBuilder) {

        return new NativeSearchQueryBuilder().withQuery(queryBuilder);
    }
}
