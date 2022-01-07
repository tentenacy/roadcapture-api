package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import org.junit.jupiter.api.Nested;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LikeApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("등록")
    class Create {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/albums/{albumId}/likes", 6L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("좋아요등록 - 성공", "좋아요등록",
                            requestHeaders(jwtHeader)));
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
            ResultActions result = mockMvc.perform(delete("/albums/{albumId}/likes", 22L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("좋아요삭제 - 성공", "좋아요삭제",
                            requestHeaders(jwtHeader)));
        }
    }
}