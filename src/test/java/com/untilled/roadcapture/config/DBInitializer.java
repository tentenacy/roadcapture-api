package com.untilled.roadcapture.config;

import com.untilled.roadcapture.api.dto.album.AlbumCreateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UserUpdateRequest;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.api.service.AlbumService;
import com.untilled.roadcapture.api.service.CommentService;
import com.untilled.roadcapture.api.service.LikeService;
import com.untilled.roadcapture.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.IntStream;

@Slf4j
@Component
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


/*
@Slf4j
@Component
@RequiredArgsConstructor
public class DBInitializer {

    private UserService userService;
    private AlbumService albumService;
    private CommentService commentService;
    private LikeService likeService;
    private PasswordEncoder passwordEncoder;
    private RestTemplate restTemplate;
    private Environment env;
    private Gson gson;
    private ObjectMapper mapper = new ObjectMapper();
    @LocalServerPort
    int randomServerPort;

    @PostConstruct
    void init() {
        String baseUrl = "http://localhost:"+randomServerPort;
        log.info("randomServerPort={}", randomServerPort);
        IntStream.range(1, 6)
                .map(i -> {
                    String encodedPassword = passwordEncoder.encode("abcd1234");
                    userService.signup(new SignupRequest("user" + i + "@gmail.com", encodedPassword, "user" + i));
                    return i;
                })
                .forEach(i -> {
                    HttpHeaders tokenHeaders = new HttpHeaders();
                    tokenHeaders.setContentType(MediaType.APPLICATION_JSON);

                    log.info("tokenHeaders={}", tokenHeaders);
                    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl+"/users/token",
                            new HttpEntity<>(
                                    MultiValueMapConverter.convert(
                                            mapper,
                                            new LoginRequest("user" + i + "@gmail.com", "abcd1234")
                                    ),
                                    tokenHeaders
                            ),
                            String.class
                    );
                    log.info("response={}", response);
                    TokenResponse token = gson.fromJson(response.getBody(), TokenResponse.class);

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("X-AUTH-TOKEN", token.getAccessToken());
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    //update
                    restTemplate.patchForObject(baseUrl+"/users",
                            new HttpEntity<>(
                                    MultiValueMapConverter.convert(
                                            mapper,
                                            new UserUpdateRequest(
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
                                            )
                                    ),
                                    headers
                            ),
                            String.class
                    );

                    restTemplate.postForEntity(baseUrl+"/albums",
                            new HttpEntity<MultiValueMap<String, String>>(
                                    MultiValueMapConverter.convert(
                                            mapper,
                                            new AlbumCreateRequest(
                                                    "볼거리가 가득한 국내 여행지 " + i,
                                                    "전국의 아름다운 10대 가로수길 중 하나로 선정된 곡교천변 은행나무길은 현충가 입구에 있습니다.",
                                                    "https://www.test.com/test",
                                                    Arrays.asList(
                                                            new PictureCreateRequest(
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
                                            )),
                                    headers
                            ),
                            String.class
                    );

                    IntStream.range(0, 5).forEach(j -> {

                        restTemplate.postForEntity(baseUrl+"/albums/pictures/"+ (5 + i * 16 - 14) +"comments",
                                new HttpEntity<MultiValueMap<String, String>>(
                                        MultiValueMapConverter.convert(
                                                mapper,
                                                new CommentCreateRequest("후기 감사합니다.")),
                                        headers
                                ),
                                String.class
                        );

                        restTemplate.postForEntity(baseUrl+"/albums/pictures/"+ (5 + i * 16 - 12) +"/comments",
                                new HttpEntity<MultiValueMap<String, String>>(
                                        MultiValueMapConverter.convert(
                                                mapper,
                                                new CommentCreateRequest("후기 감사합니다.")),
                                        headers
                                ),
                                String.class
                        );
                    });

                    restTemplate.postForEntity(baseUrl+"/albums/"+ (5 + i * 16 - 15) +"/likes",
                            new HttpEntity<MultiValueMap<String, String>>(
                                    null,
                                    headers
                            ),
                            String.class
                    );
                });
        IntStream.range(1, 6)
                .forEach(i -> {

                });

    }
}
*/
