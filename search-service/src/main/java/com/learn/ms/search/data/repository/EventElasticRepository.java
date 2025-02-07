package com.learn.ms.search.data.repository;

import com.learn.ms.search.data.entity.EventElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface EventElasticRepository extends ElasticsearchRepository<EventElastic, Long> {

}

