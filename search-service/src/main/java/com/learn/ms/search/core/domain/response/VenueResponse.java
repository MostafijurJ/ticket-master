package com.learn.ms.search.core.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueResponse implements Serializable {

    @JsonIgnore
    private Long id;
    private String name;
    private String address;
    private String location;
    private String city;
    private String country;
    private String latitude;
    private String longitude;
}
