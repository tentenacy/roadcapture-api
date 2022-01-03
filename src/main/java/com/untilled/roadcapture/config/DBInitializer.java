package com.untilled.roadcapture.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.untilled.roadcapture.api.dto.album.AlbumCreateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.dto.user.LoginRequest;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UserUpdateRequest;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.api.service.AlbumService;
import com.untilled.roadcapture.api.service.CommentService;
import com.untilled.roadcapture.api.service.LikeService;
import com.untilled.roadcapture.api.service.UserService;
import com.untilled.roadcapture.util.MultiValueMapConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.IntStream;

@Slf4j
//@Component
@RequiredArgsConstructor
public class DBInitializer {

    private final UserService userService;
    private final AlbumService albumService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        String encodedPassword = passwordEncoder.encode("abcd1234");
        IntStream.range(1, 6)
                .map(i -> {
                    userService.signup(new SignupRequest("user" + i + "@gmail.com", encodedPassword, "user" + i));
                    return i;
                })
                .forEach(i -> {
                    userService.update(Long.valueOf(i), new UserUpdateRequest(
                            null,
                            "https://www.test.com/test",
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
                    albumService.create(Long.valueOf(i), new AlbumCreateRequest(
                            "볼거리가 가득한 국내 여행지 " + i,
                            "전국의 아름다운 10대 가로수길 중 하나로 선정된 곡교천변 은행나무길은 현충가 입구에 있습니다.",
                            "https://www.test.com/test",
                            Arrays.asList(
                                    new PictureCreateRequest(
                                            LocalDateTime.now(),
                                            LocalDateTime.now(),
                                            "https://www.test.com/test",
                                            "저번에 이어 이번에도 그 목적지로 향했습니다.",
                                            new PlaceCreateRequest("곡교천 은행나무길",
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
                                    ),
                                    new PictureCreateRequest(
                                            LocalDateTime.now(),
                                            LocalDateTime.now(),
                                            "https://www.test.com/test",
                                            "저번에 이어 이번에도 그 목적지로 향했습니다.",
                                            new PlaceCreateRequest("곡교천 은행나무길",
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
                                    ))
                    ));
                    IntStream.range(0, 5).forEach(j -> {
                        commentService.create(Long.valueOf(i), Long.valueOf(5 + i * 16 - 14), new CommentCreateRequest("후기 감사합니다."));
                        commentService.create(Long.valueOf(i), Long.valueOf(5 + i * 16 - 12), new CommentCreateRequest("후기 감사합니다."));
                    });
                    likeService.create(Long.valueOf(i), Long.valueOf(5 + i * 16 - 15));
                });

    }
}
