package com.example.springbootelasticsearchexample.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

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
}
