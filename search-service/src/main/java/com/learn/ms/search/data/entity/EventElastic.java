package com.learn.ms.search.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.learn.ms.search.core.domain.enums.EventStatus;
import com.learn.ms.search.core.domain.response.PerformerResponse;
import com.learn.ms.search.core.domain.response.VenueResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "events")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventElastic implements Serializable {
    @Id
    private Long id;

    private String detailsUrl;
    private Long eventId;
    private String eventName;
    private String description;
    private Date eventDate;
    private EventStatus eventStatus;

    @Field(type = FieldType.Object)
    private VenueResponse venueResponse;

    @Field(type = FieldType.Nested)
    private List<PerformerResponse> performerResponses;
}
