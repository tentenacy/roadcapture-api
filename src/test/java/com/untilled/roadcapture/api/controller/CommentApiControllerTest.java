package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.album.AlbumCreateRequest;
import com.untilled.roadcapture.api.dto.album.AlbumUpdateRequest;
import com.untilled.roadcapture.api.dto.comment.CommentCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureCreateRequest;
import com.untilled.roadcapture.api.dto.picture.PictureUpdateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceCreateRequest;
import com.untilled.roadcapture.api.dto.place.PlaceUpdateRequest;
import org.junit.jupiter.api.Nested;

import com.untilled.roadcapture.domain.address.Address;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

class CommentApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("등록")
    class Create {

        @Test
        public void Success() throws Exception {
            //given
            CommentCreateRequest request = new CommentCreateRequest("후기 감사합니다.");

            //when
            ResultActions result = mockMvc.perform(post("/users/{userId}/albums/pictures/{pictureId}/comments",
                    2L, 52L)
                    .content(mapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("댓글등록 - 성공", "댓글등록",
                            pathParameters(commentCreatePathParams),
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
            ResultActions result = mockMvc.perform(delete("/pictures/{pictureId}/comments/{commentId}",
                    52L, 56L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("댓글삭제 - 성공", "댓글삭제",
                            pathParameters(commentDeletePathParams)));
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
            ResultActions result = mockMvc.perform(get("/albums/{albumId}/pictures/comments", 51L)
//                    .param("albumId", String.valueOf(51L))
//                    .param("pictureId", String.valueOf(52L))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("앨범댓글조회 - 성공", "앨범댓글조회",
                            pathParameters(albumCommentsPathParams),
                            requestParameters(pageParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", commentsFields)
                                    .andWithPrefix("content.[].user.", usersElementsFields)));
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
            ResultActions result = mockMvc.perform(get("/pictures/{pictureId}/comments", 52L)
//                    .param("pictureId", String.valueOf(52L))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("사진댓글조회 - 성공", "사진댓글조회",
                            pathParameters(pictureCommentsPathParams),
                            requestParameters(pageParams),
                            responseFields(pageFields)
                                    .andWithPrefix("content.[].", commentsFields)
                                    .andWithPrefix("content.[].user.", usersElementsFields)));
        }
    }

}