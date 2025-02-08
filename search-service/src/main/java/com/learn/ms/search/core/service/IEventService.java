package com.learn.ms.search.core.service;

import com.learn.ms.search.core.domain.event.EventDataForElastic;
import com.learn.ms.search.core.domain.response.EventElasticResponse;

import java.util.List;

public interface IEventService {
    void pushDataToElastic(EventDataForElastic eventDataForElastic);

    List<EventElasticResponse> searchEvents(String query);

}
