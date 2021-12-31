package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import org.junit.jupiter.api.Nested;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
        @WithMockUser(username = "mockUser")
        public void Success() throws Exception {
            //given
            CommentCreateRequest request = new CommentCreateRequest("후기 감사합니다.");

            //when
            ResultActions result = mockMvc.perform(post("/users/{userId}/albums/pictures/{pictureId}/comments",
                    2L, 7L)
                    .content(mapper.writeValueAsString(request))
                    .header("X-AUTH-TOKEN", "")
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("댓글등록 - 성공", "댓글등록",
                            requestHeaders(jwtHeader),
                            pathParameters(commentCreatePathParams),
                            requestFields(commentCreateRequestFields)));
        }
    }

    @Nested
    @DisplayName("삭제")
    class Delete {

        @Test
        @WithMockUser(username = "mockUser")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(delete("/pictures/{pictureId}/comments/{commentId}",
                    7L, 11L)
                    .header("X-AUTH-TOKEN", "")
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("댓글삭제 - 성공", "댓글삭제",
                            requestHeaders(jwtHeader),
                            pathParameters(commentDeletePathParams)));
        }
    }

    @Nested
    @DisplayName("앨범댓글조회")
    class AlbumComments {

        @Test
        @WithMockUser(username = "mockUser")
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/albums/{albumId}/pictures/comments", 6L)
//                    .param("albumId", String.valueOf(51L))
//                    .param("pictureId", String.valueOf(52L))
                    .header("X-AUTH-TOKEN", "")
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("앨범댓글조회 - 성공", "앨범댓글조회",
                            requestHeaders(jwtHeader),
                            pathParameters(albumCommentsPathParams),
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
        @WithMockUser(username = "mockUser")
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(get("/pictures/{pictureId}/comments", 7L)
//                    .param("pictureId", String.valueOf(52L))
                    .header("X-AUTH-TOKEN", "")
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("사진댓글조회 - 성공", "사진댓글조회",
                            requestHeaders(jwtHeader),
                            pathParameters(pictureCommentsPathParams),
                            requestParameters(pageParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", commentsFields)
                                    .andWithPrefix("content.[].user.", usersFields)));
        }
    }

}