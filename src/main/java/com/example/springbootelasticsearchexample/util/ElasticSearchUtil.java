package com.example.springbootelasticsearchexample.util;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** [ Util 작업을 하면서 느끼는 것 ]
 *
 * 1.
 * boolQuery는 Util에서 만들기보다는 Service Layer에서 직접 만드는 것이 비즈니스에 더 적합할 것.
 * Util 클래스에서는 쿼리의 최소 단위들을 만드는 조각들을 쉽게 생성하는 메서드들이 있는 것이 좋겠다.
 * (ex, Match, MatchAll, Term, Range...)
 *
 * 2.
 * Supplier도 굳이 Util에서 리턴할 필요가 없을 것 같다.
 * 어차피 비즈니스에 필요한 파라미터를 받아와야 하는 상황에서는 Service Layer에서 직접 만드는 것이 더 유연하겠다.
 */
@Slf4j
public class ElasticSearchUtil {

    /*
    {
      "query": {
        "match_all": { <-- [ supplier() ]
            공백(블럭) <-- [ matchAllQuery() ]
        }
      }
    }
    */
    public static Supplier<Query> supplier() {
        Supplier<Query> supplier = () -> Query.of(q -> q.matchAll(matchAllQuery()));
        log.info("supplier={}", supplier.get().toString()); // supplier=Query: {"match_all":{}}
        return supplier;
    }

    public static MatchAllQuery matchAllQuery() {
        MatchAllQuery.Builder query = new MatchAllQuery.Builder();
        MatchAllQuery matchAllQuery = query.build();
        log.info("query={}", matchAllQuery); // query=MatchAllQuery: {}
        return matchAllQuery;
    }


    /*
    {
      "query": {
        "match": { <-- [ supplierWithNameField() ]
          "name": { <-- [ matchQueryWithNameField() ]
            "query": "mobile"
          }
        }
      }
    }
    */
    public static Supplier<Query> supplierWithNameField(String fieldValue) {
        Supplier<Query> supplier = () -> Query.of(q -> q.match(matchQueryWithNameField(fieldValue)));
        log.info("supplier={}", supplier.get().toString()); // supplier=Query: {"match":{"name":{"query":"mobile"}}}
        return supplier;
    }

    public static MatchQuery matchQueryWithNameField(String fieldValue) {
        MatchQuery.Builder query = new MatchQuery.Builder();
        MatchQuery matchQuery = query.field("name").query(fieldValue).build();
        log.info("matchQuery={}", matchQuery.toString()); // matchQuery=MatchQuery: {"name":{"query":"mobile"}}
        return matchQuery;
    }

    /*
    {
      "query": {
        "bool": {
          "filter": [
            {
              "term": {
                "name": "charger"
              }
            }
          ],
          "must": [
            {
              "range": {
                "price": {
                  "lt": 5000,
                  "gt": 1000
                }
              }
            }
          ]
        }
      }
    }
    */
    public static Supplier<Query> boolQuerySupplier(String termQueryFieldValue,
                                                    Integer lt,
                                                    Integer gt) {
        Supplier<Query> supplier = () -> Query.of(q -> q.bool(boolQuery(termQueryFieldValue, lt, gt)));
        log.info("supplier={}", supplier.get().toString());
        return supplier;
    }

    public static BoolQuery boolQuery(String termQueryFieldValue,
                                      Integer lt,
                                      Integer gt) {
        List<Query> filterQueryList = new ArrayList<>();
        filterQueryList.add(termQuery(termQueryFieldValue)._toQuery());

        List<Query> mustQueryList = new ArrayList<>();
        mustQueryList.add(rangeQueryWithPrice(lt, gt)._toQuery());

        BoolQuery.Builder query = new BoolQuery.Builder();
        BoolQuery boolQuery = query.filter(filterQueryList).must(mustQueryList).build();
        return boolQuery;
    }

    public static TermQuery termQuery(String fieldValue) {
        TermQuery.Builder query = new TermQuery.Builder();
        TermQuery termQuery = query.field("name").value(fieldValue).build();
        log.info("termQuery={}", termQuery.toString());
        return termQuery;
    }

    public static RangeQuery rangeQueryWithPrice(Integer lt, Integer gt) {
        RangeQuery.Builder query = new RangeQuery.Builder();
        RangeQuery rangeQuery = query.field("price")
                .lt(JsonData.of(lt))
                .gt(JsonData.of(gt))
                .build();
        log.info("rangeQuery={}", rangeQuery.toString());
        return rangeQuery;
    }

}
