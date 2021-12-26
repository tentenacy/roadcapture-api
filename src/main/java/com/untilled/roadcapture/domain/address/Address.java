package com.untilled.roadcapture.domain.address;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    private String addressName;
    private String roadAddressName;
    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;
    private String zoneNo;

}
