package com.untilled.roadcapture.config;

import com.untilled.roadcapture.api.dto.album.TempAlbumCreateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UserUpdateRequest;
import com.untilled.roadcapture.api.service.AlbumService;
import com.untilled.roadcapture.api.service.CommentService;
import com.untilled.roadcapture.api.service.LikeService;
import com.untilled.roadcapture.api.service.UserService;
import com.untilled.roadcapture.domain.address.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.IntStream;

@ActiveProfiles({"test"})
@Slf4j
@Component
@RequiredArgsConstructor
public class TestDBInitializer {

    private final UserService userService;
    private final AlbumService albumService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        String encodedPassword = passwordEncoder.encode("abcd1234");
        IntStream.range(1, 6)
                .peek(i -> userService.signup(new SignupRequest("user" + i + "@gmail.com", encodedPassword, "유저" + i)))
                .forEach(i -> {
                    userService.update((long) i, new UserUpdateRequest(
                            null,
                            "https://az360.school/public/files/users/full/b52e290c_free-profile-photo-whatsapp-4.png",
                            "https://usercontents-d.styleshare.io/images/46615561/1280x-",
                            "안녕하세요. 저는 user" + i + "입니다.",
                            new Address("경기 시흥시 정왕동 2121-1 한국산업기술대학교",
                                    "경기 시흥시 산기대학로 237 한국산업기술대학교",
                                    "경기도",
                                    "시흥시",
                                    "정왕동",
                                    "15073"
                            )
                    ));
                });
        IntStream.range(1, 6)
                .forEach(i -> {
                    albumService.create((long) i, new TempAlbumCreateRequest(
                            "맑은 하늘 아래 함께 떠나요 - " + i,
                            "제가 다닌 흔적을 따라 함께 여행하는 기분을 느껴보세요.",
                            Arrays.asList(
                                    new TempPictureCreateRequest(
                                            true,
                                            0,
                                            "https://static-storychat.pstatic.net/2516396_30992988/ebgea7g354b40.jpg",
                                            "맑은 하늘",
                                            new PlaceCreateRequest("양곡신협 주변",
                                                    37.65450778860656,
                                                    126.62440348079012,
                                                    new Address(
                                                            "경기도 김포시 양촌읍 양곡리 408-1",
                                                            "양곡1로40번길 13",
                                                            "경기도",
                                                            "김포시",
                                                            "양촌읍",
                                                            "408-1"
                                                    )
                                            )
                                    ),
                                    new TempPictureCreateRequest(
                                            false,
                                            1,
                                            "https://static-storychat.pstatic.net/2516396_30992988/ebgea5nc5blb0.jpg",
                                            "멋쟁이 고양이",
                                            new PlaceCreateRequest("양곡휴먼시아7단지아파트",
                                                    37.65533081566674,
                                                    126.63105692431945,
                                                    new Address(
                                                            "경기도 김포시 양촌읍 양곡리 1316",
                                                            "양곡2로30번길 77",
                                                            "경기도",
                                                            "김포시",
                                                            "양촌읍",
                                                            "10062"
                                                    )
                                            )
                                    ))
                    ));
                    IntStream.range(0, 5).forEach(j -> {
                        commentService.create((long) i, 5 + i * 16L - 14, new CommentCreateRequest("후기 감사합니다."));
                        commentService.create((long) i, 5 + i * 16L - 12, new CommentCreateRequest("후기 감사합니다."));
                    });
                    likeService.create((long) i, 5 + i * 16L - 15);
                });

    }
}
