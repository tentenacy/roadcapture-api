package com.untilled.roadcapture.api.dto.album;

import com.untilled.roadcapture.api.dto.place.PlaceCondition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserAlbumsCondition {
    
    private PlaceCondition placeCond;
}
