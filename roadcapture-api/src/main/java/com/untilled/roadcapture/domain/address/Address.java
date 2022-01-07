package com.untilled.roadcapture.domain.address;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    @NotEmpty
    private String addressName;
    private String roadAddressName;
    @NotEmpty
    private String region1DepthName;
    @NotEmpty
    private String region2DepthName;
    @NotEmpty
    private String region3DepthName;
    @NotEmpty
    private String zoneNo;
}
