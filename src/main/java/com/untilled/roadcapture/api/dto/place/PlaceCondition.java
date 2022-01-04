package com.untilled.roadcapture.api.dto.place;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlaceCondition {

    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;
}
