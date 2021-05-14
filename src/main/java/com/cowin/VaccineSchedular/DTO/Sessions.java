package com.cowin.VaccineSchedular.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Sessions {
    private String name;
    private Integer availableCapacity;
    private Integer minAgeLimit;
    private String vaccine;
    private String Address;
}
