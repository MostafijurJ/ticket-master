package com.learn.ms.event.core.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String location;
    private String city;
    private String country;
    private String latitude;
    private String longitude;
}
