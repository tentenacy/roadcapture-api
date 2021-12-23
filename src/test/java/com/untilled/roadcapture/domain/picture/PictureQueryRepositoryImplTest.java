package com.untilled.roadcapture.domain.picture;

import com.untilled.roadcapture.api.exception.PictureNotFoundException;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PictureQueryRepositoryImplTest {

    @Autowired
    private PictureRepository pictureRepository;

    @Test
    public void getPicturesByAlbumId() throws Exception {
        //given

        //when
        List<Picture> pictures = pictureRepository.getPicturesByAlbumId(171L).orElseThrow(PictureNotFoundException::new);

        //then
        for (Picture picture : pictures) {
            System.out.println("picture.id = " + picture.getId());
        }
    }
}