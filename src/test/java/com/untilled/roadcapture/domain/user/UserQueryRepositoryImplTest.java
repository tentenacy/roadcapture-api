package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.user.UsersCondition;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.domain.base.BaseDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.*;

class UserQueryRepositoryImplTest extends BaseDataJpaTest {

    @Test
    public void search() throws Exception {
        //given
        String textToSearch = "use";

        //when
        Page<UsersResponse> usersPage = userRepository.search(PageRequest.of(0, 10, Sort.by("createdAt").descending()), new UsersCondition(textToSearch));

        //then
        assertThat(usersPage.getContent().size()).isEqualTo(3);
        usersPage.forEach(usersResponse -> {
            assertThat(usersResponse.getUsername()).contains(textToSearch);
        });
    }
}