package com.untilled.roadcapture.domain.user;

import com.untilled.roadcapture.api.dto.user.UsersCondition;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.config.RepoTestConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RepoTestConfiguration.class)
class UserQueryRepositoryImplTest {

    @Autowired
    private UserRepository repo;

    @BeforeAll
    public void setup() {

        IntStream.range(1, 6)
                        .forEach(i -> {
                            repo.save(User.create("user" + i + "@gmail.com", "abcd1234", "user" + i));
                            repo.save(User.create("user" + i + "@gmail.com", "abcd1234", "test" + i));
                        });
    }

    @Test
    public void search() throws Exception {
        //given
        String textToSearch = "use";

        //when
        Page<UsersResponse> usersPage = repo.search(PageRequest.of(0, 10, Sort.by("createdAt").descending()), new UsersCondition(textToSearch));

        //then
        assertThat(usersPage.getContent().size()).isEqualTo(5);
        usersPage.forEach(usersResponse -> {
            assertThat(usersResponse.getUsername()).contains(textToSearch);
        });
    }
}