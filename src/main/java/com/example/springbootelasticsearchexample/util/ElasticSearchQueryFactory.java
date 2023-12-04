package com.example.springbootelasticsearchexample.util;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ElasticSearchQueryFactory {


    public static BoolQuery.Builder boolQueryBuilder() {
        return new BoolQuery.Builder();
    }

    public static MatchAllQuery matchAllQuery() {
        MatchAllQuery.Builder query = new MatchAllQuery.Builder();
        MatchAllQuery matchAllQuery = query.build();
        log.info("query={}", matchAllQuery);
        return matchAllQuery;
    }


    public static MatchQuery matchQuery(String fieldName, String fieldValue) {
        MatchQuery.Builder query = new MatchQuery.Builder();
        MatchQuery matchQuery = query.field(fieldName).query(fieldValue).build();
        log.info("matchQuery={}", matchQuery.toString());
        return matchQuery;
    }

    public static TermQuery termQuery(String fieldName, String fieldValue) {
        TermQuery.Builder query = new TermQuery.Builder();
        TermQuery termQuery = query.field(fieldName).value(fieldValue).build();
        log.info("termQuery={}", termQuery.toString());
        return termQuery;
    }

    /**
     * @param fieldName 필드명
     * @param ltValue 상한값
     * @param ltEqual true: 이하, false: 미만
     * @param gtValue 하한값
     * @param gtEqual true: 이상, false: 초과
     */
    public static RangeQuery rangeQueryLessThanGraterThan(String fieldName,
                                                          Integer ltValue,
                                                          Boolean ltEqual,
                                                          Integer gtValue,
                                                          Boolean gtEqual) {
        RangeQuery.Builder query = new RangeQuery.Builder();
        RangeQuery rangeQuery = query.field(fieldName).build();

        JsonData value1 = JsonData.of(ltValue);
        if (ltEqual) {
            query.lte(value1);
        } else {
            query.lt(value1);
        }

        JsonData value2 = JsonData.of(gtValue);
        if (gtEqual) {
            query.gte(value2);
        } else {
            query.gt(value2);
        }

        log.info("rangeQuery={}", rangeQuery.toString());
        return rangeQuery;
    }
}
