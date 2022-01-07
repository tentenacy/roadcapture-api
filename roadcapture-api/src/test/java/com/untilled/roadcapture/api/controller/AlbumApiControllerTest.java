package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.album.AlbumCreateRequest;
import com.untilled.roadcapture.api.dto.album.AlbumUpdateRequest;
import com.untilled.roadcapture.api.dto.album.TempAlbumCreateRequest;
import com.untilled.roadcapture.api.dto.album.TempAlbumUpdateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.TempPictureUpdateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import com.untilled.roadcapture.config.SpringBootTestConfiguration;
import com.untilled.roadcapture.domain.address.Address;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlbumApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("등록")
    class Create {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            TempAlbumCreateRequest request = new TempAlbumCreateRequest(
                    "볼거리가 가득한 국내 여행지",
                    "전국의 아름다운 10대 가로수길 중 하나로 선정된 곡교천변 은행나무길은 현충가 입구에 있습니다.",
                    Arrays.asList(new TempPictureCreateRequest(
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
                    ))
            );

            //when
            ResultActions result = mockMvc.perform(post("/albums/temp")
                    .content(mapper.writeValueAsString(request))
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("앨범등록 - 성공", "앨범등록",
                            requestHeaders(jwtHeader),
                            requestFields(albumCreateRequestFields)
                                    .andWithPrefix("pictures.[].", pictureCreateRequestFields)
                                    .andWithPrefix("pictures.[].place.", placeCreateRequestFields)
                                    .andWithPrefix("pictures.[].place.address.", addressFields)
                    ));
        }
    }

    @Nested
    @DisplayName("조회")
    class Albums {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/albums")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[1].commentCount").value(10L))
                    .andExpect(jsonPath("$.content[1].liked").value(true))
                    .andDo(document("앨범조회 - 성공", "앨범조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(albumsParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", albumsFields)
                                    .andWithPrefix("content.[].user.", usersFields)));
        }
    }

    @Nested
    @DisplayName("팔로잉앨범조회")
    class FollowingAlbums {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            followerService.create(2L, 1L);
            followerService.create(2L, 3L);
            followerService.create(2L, 4L);
            followerService.create(2L, 5L);

            //when
            ResultActions result = mockMvc.perform(get("/followers/to/albums")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(4))
                    .andDo(document("팔로잉앨범조회 - 성공", "팔로잉앨범조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(followingAlbumsParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", albumsFields)
                                    .andWithPrefix("content.[].user.", usersFields)));
        }

        @Test
        @DisplayName("팔로잉 아이디 조건 추가 성공")
        public void FollowingIdAdded_Success() throws Exception {
            //given
            followerService.create(2L, 1L);
            followerService.create(2L, 3L);
            followerService.create(2L, 4L);
            followerService.create(2L, 5L);

            //when
            ResultActions result = mockMvc.perform(get("/followers/to/albums")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .queryParam("followingId", "1")
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andDo(document("팔로잉앨범조회 - 팔로잉 아이디 조건 추가 성공", "팔로잉앨범조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(followingAlbumsParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", albumsFields)
                                    .andWithPrefix("content.[].user.", usersFields)));
        }
    }

    @Nested
    @DisplayName("단건조회")
    class Album {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/albums/{id}", 6L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(6L))
                    .andExpect(jsonPath("$.pictures[0].id").value(7L))
                    .andExpect(jsonPath("$.pictures[1].id").value(9L))
                    .andExpect(jsonPath("$.commentCount").value(10L))
                    .andDo(document("앨범단건조회 - 성공", "앨범단건조회",
                            requestHeaders(jwtHeader),
                            pathParameters(albumPathParams),
                            responseFields(albumFields)
                                    .andWithPrefix("user.", albumUserFields)
                                    .andWithPrefix("pictures.[].", pictureFields)
                                    .andWithPrefix("pictures.[].place.", placeFields)
                                    .andWithPrefix("pictures.[].place.address.", addressFields)));
        }
    }

    @Nested
    @DisplayName("유저앨범조회")
    class UserAlbums {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/users/albums")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("유저앨범조회 - 성공", "유저앨범조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(userAlbumsParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", userAlbumsFields)
                                    .andWithPrefix("content.[].thumbnailPicture.", thumbnailPictureFields)
                                    .andWithPrefix("content.[].thumbnailPicture.place.", placeFields)
                                    .andWithPrefix("content.[].thumbnailPicture.place.address.", addressFields)));
        }

        @Test
        @DisplayName("시도명으로 검색 성공")
        public void SearchByRegion1DepthName_Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/users/albums")
                    .queryParam("placeCond.region1DepthName", "경기")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].thumbnailPicture.place.address.region1DepthName").value("경기"))
                    .andDo(document("유저앨범조회 - 시도명으로 검색 성공", "유저앨범조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(userAlbumsParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", userAlbumsFields)
                                    .andWithPrefix("content.[].thumbnailPicture.", thumbnailPictureFields)
                                    .andWithPrefix("content.[].thumbnailPicture.place.", placeFields)
                                    .andWithPrefix("content.[].thumbnailPicture.place.address.", addressFields)));
        }

        @Test
        @DisplayName("시군구명으로 검색 성공")
        public void SearchByRegion2DepthName_Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/users/albums")
                    .queryParam("placeCond.region2DepthName", "시흥시")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].thumbnailPicture.place.address.region2DepthName").value("시흥시"))
                    .andDo(document("유저앨범조회 - 시군구명으로 검색 성공", "유저앨범조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(userAlbumsParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", userAlbumsFields)
                                    .andWithPrefix("content.[].thumbnailPicture.", thumbnailPictureFields)
                                    .andWithPrefix("content.[].thumbnailPicture.place.", placeFields)
                                    .andWithPrefix("content.[].thumbnailPicture.place.address.", addressFields)));
        }

        @Test
        @DisplayName("읍면동명으로 검색 성공")
        public void SearchByRegion3DepthName_Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/users/albums")
                    .queryParam("placeCond.region3DepthName", "염치읍")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(0))
                    .andDo(document("유저앨범조회 - 읍면동명으로 검색 성공", "유저앨범조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(userAlbumsParams)));
        }
    }

    @Nested
    @DisplayName("수정")
    class Update {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            TempAlbumUpdateRequest request = new TempAlbumUpdateRequest(
                    "볼거리가 가득한 국내 여행지!!!",
                    "전국의 아름다운 10대 가로수길 중 하나로 선정된 곡교천변 은행나무길은 현충가 입구에 있습니다.",
                    Arrays.asList(new TempPictureUpdateRequest(
                            23L,
                            true,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            "https://www.test.com/test",
                            "저번에 이어 이번에도 그 목적지로 향했습니다!!!",
                            new PlaceUpdateRequest(
                                    "그 은행나무!!!",
                                    LocalDateTime.now(),
                                    LocalDateTime.now(),
                                    36.1112512,
                                    27.1146346,
                                    new Address(
                                            "충남 아산시 염치읍 백암리 502-3",
                                            "",
                                            "충남",
                                            "아산시",
                                            "염치읍",
                                            "336-813"
                                    )
                            )
                    ))
            );

            //when
            ResultActions result = mockMvc.perform(put("/albums/{id}/temp", 22L)
                    .content(mapper.writeValueAsString(request))
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("앨범수정 - 성공", "앨범수정",
                            requestHeaders(jwtHeader),
                            pathParameters(albumPathParams),
                            requestFields(albumUpdateRequestFields)
                                    .andWithPrefix("pictures.[].", pictureUpdateRequestFields)
                                    .andWithPrefix("pictures.[].place.", placeUpdateRequestFields)
                                    .andWithPrefix("pictures.[].place.address.", addressFields)));
        }
    }

    @Nested
    @DisplayName("삭제")
    class Delete {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(delete("/albums/{id}", 22L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("앨범삭제 - 성공", "앨범삭제",
                            requestHeaders(jwtHeader),
                            pathParameters(albumPathParams)));

        }
    }

}