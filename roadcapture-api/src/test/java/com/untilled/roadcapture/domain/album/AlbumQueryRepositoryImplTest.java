package com.untilled.roadcapture.domain.album;

import com.untilled.roadcapture.api.dto.album.AlbumsCondition;
import com.untilled.roadcapture.api.dto.album.AlbumsResponse;
import com.untilled.roadcapture.api.dto.user.UsersCondition;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.config.RepoTestConfiguration;
import com.untilled.roadcapture.domain.base.BaseDataJpaTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AlbumQueryRepositoryImplTest extends BaseDataJpaTest {

    /*@Test
    public void search() throws Exception {
        //given
        String textToSearch = "볼거리";

        //when
        Page<AlbumsResponse> albumsPage = albumRepository.search(new AlbumsCondition(null, null, textToSearch), PageRequest.of(0, 10, Sort.by("createdAt").descending()));

        //then
        assertThat(albumsPage.getContent().size()).isEqualTo(3);
        albumsPage.forEach(albumsResponse -> {
            assertThat(albumsResponse.getTitle()).contains(textToSearch);
        });
    }*/
}