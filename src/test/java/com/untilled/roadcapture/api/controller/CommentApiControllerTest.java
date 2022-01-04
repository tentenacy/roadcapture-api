package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;

import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import org.junit.jupiter.api.Nested;

import org.junit.jupiter.api.*;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("등록")
    class Create {

        @Test
        public void Success() throws Exception {
            //given
            CommentCreateRequest request = new CommentCreateRequest("후기 감사합니다.");

            //when
            ResultActions result = mockMvc.perform(post("/pictures/{pictureId}/comments", 7L)
                    .content(mapper.writeValueAsString(request))
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("댓글등록 - 성공", "댓글등록",
                            requestHeaders(jwtHeader),
                            requestFields(commentCreateRequestFields)));
        }
    }

    @Nested
    @DisplayName("삭제")
    class Delete {

        @Test
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(delete("/comments/{commentId}", 11L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("댓글삭제 - 성공", "댓글삭제",
                            requestHeaders(jwtHeader)));
        }
    }

    @Nested
    @DisplayName("앨범댓글조회")
    class AlbumComments {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/albums/{albumId}/pictures/comments", 6L)
//                    .param("albumId", String.valueOf(51L))
//                    .param("pictureId", String.valueOf(52L))
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("앨범댓글조회 - 성공", "앨범댓글조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", commentsFields)
                                    .andWithPrefix("content.[].user.", usersFields)));
        }
    }

    @Nested
    @DisplayName("사진댓글조회")
    class PictureComments {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/pictures/{pictureId}/comments", 7L)
//                    .param("pictureId", String.valueOf(52L))
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("사진댓글조회 - 성공", "사진댓글조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", commentsFields)
                                    .andWithPrefix("content.[].user.", usersFields)));
        }
    }

}