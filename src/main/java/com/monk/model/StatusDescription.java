package com.monk.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusDescription {
    private Integer statusCode;
    private String statusDescription;
}
