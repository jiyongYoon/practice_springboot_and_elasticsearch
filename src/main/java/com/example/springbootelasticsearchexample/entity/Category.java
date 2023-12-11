package com.example.springbootelasticsearchexample.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document(indexName = "categories", createIndex = true)
@Setting(settingPath = "/elasticsearch/settings/categories_settings.json") // 세팅 정보 작성
@Mapping(mappingPath = "/elasticsearch/mappings/categories_mappings.json") // 매핑 정보 작성 => 자동생성 매핑정보가 아니라 지정할 수 있음.
public class Category {
    @Id
    Long id;
    String categoryName;
}
