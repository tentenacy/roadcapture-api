package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.album.AlbumCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import org.junit.jupiter.api.Nested;

import com.untilled.roadcapture.domain.address.Address;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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

            //when
            ResultActions result = mockMvc.perform(post("/albums")
                    .content(mapper.writeValueAsString(new AlbumCreateRequest(
                            "볼거리가 가득한 국내 여행지",
                            "전국의 아름다운 10대 가로수길 중 하나로 선정된 곡교천변 은행나무길은 현충가 입구에 있습니다.",
                            "https://www.test.com/test",
                            Arrays.asList(new PictureCreateRequest(
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
                            )),
                            101L
                    )))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("앨범등록 - 성공",
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
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("앨범조회 - 성공",
                            requestParameters(pageParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", albumsFields)
                                    .andWithPrefix("content.[].user.", usersElementsFields)));
        }
    }

}