package com.example.springbootelasticsearchexample.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

/*
자동생성된 인덱스에서 _search query를 날리면,
source안에는 아래 필드로 선언된 내용 외에 한가지가 더 들어가있다.
"_source": {
  "_class": "com.example.springbootelasticsearchexample.entity.Product",
...
이 필드 때문에 JsonIgnore를 해줘야 에러가 나지 않는다.
(아니면 mappings 정보를 넣어서 직접 인덱스를 만들어주면 _class 필드는 생기지 않는다)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document(indexName = "products")
public class Product {
    private int id;
    private String name;
    private String description;
    private int quantity;
    private double price;
}
