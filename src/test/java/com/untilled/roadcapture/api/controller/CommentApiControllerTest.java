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

    protected FieldDescriptor[] commentCreateRequestFields = new FieldDescriptor[]{
            fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용입니다."),
    };

    protected ParameterDescriptor[] commentCreatePathParams = new ParameterDescriptor[]{
            parameterWithName("userId").description("사용자 아이디입니다."),
            parameterWithName("albumId").description("앨범 아이디입니다."),
            parameterWithName("pictureId").description("사진 아이디입니다."),
    };

    protected ParameterDescriptor[] commentDeletePathParams = new ParameterDescriptor[]{
            parameterWithName("pictureId").description("사진 아이디입니다."),
            parameterWithName("commentId").description("댓글 아이디입니다."),
    };



    @Nested
    @DisplayName("등록")
    class Create {

        @Test
        public void Success() throws Exception {
            //given
            CommentCreateRequest request = new CommentCreateRequest("후기 감사합니다.");

            //when
            ResultActions result = mockMvc.perform(post("/users/{userId}/albums/{albumId}/pictures/{pictureId}/comments",
                    2L, 51L, 52L)
                    .content(mapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("댓글등록 - 성공",
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
                    52L, 54L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("댓글삭제 - 성공",
                            pathParameters(commentDeletePathParams)));
        }
    }

}