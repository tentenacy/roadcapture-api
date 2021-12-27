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

class LikeApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("등록")
    class Create {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/{userId}/albums/{albumId}/likes",
                    2L, 51L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("좋아요등록 - 성공", "좋아요등록",
                            pathParameters(likePathParams)));
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
            ResultActions result = mockMvc.perform(delete("/users/{userId}/albums/{albumId}/likes",
                    1L, 51L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("좋아요삭제 - 성공", "좋아요삭제",
                            pathParameters(likePathParams)));
        }
    }
}