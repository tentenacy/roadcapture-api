package com.untilled.roadcapture.domain.base;

import com.untilled.roadcapture.api.dto.album.AlbumCreateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.config.RepoTestConfiguration;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.album.Album;
import com.untilled.roadcapture.domain.album.AlbumRepository;
import com.untilled.roadcapture.domain.comment.Comment;
import com.untilled.roadcapture.domain.comment.CommentRepository;
import com.untilled.roadcapture.domain.like.Like;
import com.untilled.roadcapture.domain.like.LikeRepository;
import com.untilled.roadcapture.domain.picture.PictureRepository;
import com.untilled.roadcapture.domain.place.PlaceRepository;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ActiveProfiles({"test"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RepoTestConfiguration.class)
public class BaseDataJpaTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AlbumRepository albumRepository;

    @Autowired
    protected PictureRepository pictureRepository;

    @Autowired
    protected PlaceRepository placeRepository;

    @Autowired
    protected LikeRepository likeRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @BeforeAll
    public void setup() {
        IntStream.range(1, 4)
                .forEach(i -> {
                    User createdUser = userRepository.save(createUser(i));
                    User createdAnotherUser = userRepository.save(createAnotherUser(i));
                    Album createdAlbum = albumRepository.save(createAlbum(i, createdUser));
                    Album createdAnotherAlbum = albumRepository.save(createAnotherAlbum(i, createdAnotherUser));
                    IntStream.range(0, 3)
                            .forEach(j -> {
                                createdAlbum.getPictures().stream()
                                        .forEach(picture -> {
                                            picture.addComment(createComment(createdUser));
                                        });
                                createdAlbum.addLike(Like.create(createdUser));
                            });
                    albumRepository.save(createdAlbum);
                });
    }

    @AfterAll
    public void tearDown() {
        userRepository.deleteAll();
        albumRepository.deleteAll();
        pictureRepository.deleteAll();
        placeRepository.deleteAll();
        likeRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @NotNull
    private Comment createComment(User createdUser) {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("후기 감사합니다.");
        Comment comment = Comment.create(commentCreateRequest.getContent(), createdUser);
        return comment;
    }

    private User createUser(int i) {
        return User.create("user" + i + "@gmail.com", "abcd1234", "user" + i);
    }

    private User createAnotherUser(int i) {
        return User.create("test" + i + "@gmail.com", "abcd1234", "test" + i);
    }

    private Album createAlbum(int i, User createdUser) {
        AlbumCreateRequest albumCreateRequest = new AlbumCreateRequest(
                "볼거리가 가득한 국내 여행지 " + i,
                "전국의 아름다운 10대 가로수길 중 하나로 선정된 곡교천변 은행나무길은 현충가 입구에 있습니다.",
                Arrays.asList(
                        new TempPictureCreateRequest(
                                true,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                "https://www.test.com/test",
                                "저번에 이어 이번에도 그 목적지로 향했습니다.",
                                new PlaceCreateRequest("곡교천 은행나무길",
                                        LocalDateTime.now(),
                                        LocalDateTime.now(),
                                        36.1112512,
                                        27.1146346,
                                        new Address(
                                                "충남 아산시 염치읍 백암리 502-3",
                                                null,
                                                "충남",
                                                "아산시",
                                                "염치읍",
                                                "336-813"
                                        )
                                )
                        ).toPictureCreateRequest(),
                        new TempPictureCreateRequest(
                                false,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                "https://www.test.com/test",
                                "저번에 이어 이번에도 그 목적지로 향했습니다.",
                                new PlaceCreateRequest("곡교천 은행나무길",
                                        LocalDateTime.now(),
                                        LocalDateTime.now(),
                                        36.1112512,
                                        27.1146346,
                                        new Address(
                                                "충남 아산시 염치읍 백암리 502-3",
                                                null,
                                                "충남",
                                                "아산시",
                                                "염치읍",
                                                "336-813"
                                        )
                                )
                        ).toPictureCreateRequest())
        );
        return Album.create(albumCreateRequest.getTitle(), albumCreateRequest.getDescription(), albumCreateRequest.getPictures().stream().map(picture -> picture.toEntity()).collect(Collectors.toList()), createdUser);
    }

    private Album createAnotherAlbum(int i, User createdUser) {
        AlbumCreateRequest albumCreateRequest = new AlbumCreateRequest(
                "먹거리가 가득한 국내 여행지 " + i,
                "전국의 아름다운 10대 가로수길 중 하나로 선정된 곡교천변 은행나무길은 현충가 입구에 있습니다.",
                Arrays.asList(
                        new TempPictureCreateRequest(
                                true,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                "https://www.test.com/test",
                                "저번에 이어 이번에도 그 목적지로 향했습니다.",
                                new PlaceCreateRequest("곡교천 은행나무길",
                                        LocalDateTime.now(),
                                        LocalDateTime.now(),
                                        36.1112512,
                                        27.1146346,
                                        new Address(
                                                "충남 아산시 염치읍 백암리 502-3",
                                                null,
                                                "충남",
                                                "아산시",
                                                "염치읍",
                                                "336-813"
                                        )
                                )
                        ).toPictureCreateRequest(),
                        new TempPictureCreateRequest(
                                false,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                "https://www.test.com/test",
                                "저번에 이어 이번에도 그 목적지로 향했습니다.",
                                new PlaceCreateRequest("곡교천 은행나무길",
                                        LocalDateTime.now(),
                                        LocalDateTime.now(),
                                        36.1112512,
                                        27.1146346,
                                        new Address(
                                                "충남 아산시 염치읍 백암리 502-3",
                                                null,
                                                "충남",
                                                "아산시",
                                                "염치읍",
                                                "336-813"
                                        )
                                )
                        ).toPictureCreateRequest())
        );
        return Album.create(albumCreateRequest.getTitle(), albumCreateRequest.getDescription(), albumCreateRequest.getPictures().stream().map(picture -> picture.toEntity()).collect(Collectors.toList()), createdUser);
    }
}
