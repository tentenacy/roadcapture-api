package com.untilled.roadcapture.domain.address;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String addressName;
    private String roadAddressName;
    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;
    private String zoneNo;

    protected Address() { }

    public Address(String addressName, String roadAddressName, String region1DepthName, String region2DepthName, String region3DepthName, String zoneNo) {
        this.addressName = addressName;
        this.roadAddressName = roadAddressName;
        this.region1DepthName = region1DepthName;
        this.region2DepthName = region2DepthName;
        this.region3DepthName = region3DepthName;
        this.zoneNo = zoneNo;
    }
}
