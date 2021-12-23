package com.untilled.roadcapture.domain.place;

import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import com.untilled.roadcapture.api.exception.PlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Transactional
    public Place create(PlaceCreateRequest request) {
        return placeRepository.save(Place.create(request.getName(), request.getLatitude(), request.getLongitude(), request.getAddress()));
    }

    @Transactional
    public void update(Long placeId, PlaceUpdateRequest request) {
        getPlaceIfExists(placeId).update(request.getName(), request.getLatitude(), request.getLongitude(), request.getAddress());
    }

    @Transactional
    public void delete(Long placeId) {
        placeRepository.delete(getPlaceIfExists(placeId));
    }

    private Place getPlaceIfExists(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    }
}
