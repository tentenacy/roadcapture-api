package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FollowerApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("팔로우")
    class Follow {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/followers/{toUserId}", 1L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("팔로우 - 성공", "팔로우",
                            requestHeaders(jwtHeader)));
        }
    }

    @Nested
    @DisplayName("언팔로우")
    class Unfollow {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            followerService.create(2L, 1L);

            //when
            ResultActions result = mockMvc.perform(delete("/followers/{toUserId}", 1L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            assertThat(followerRepository.getFollowerByFromIdAndToId(2L, 1L).orElse(null)).isNull();

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("언팔로우 - 성공", "언팔로우",
                            requestHeaders(jwtHeader)));
        }
    }

    @Nested
    @DisplayName("팔로워조회")
    class Followers {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            followerService.create(1L, 2L);
            followerService.create(3L, 2L);
            followerService.create(4L, 2L);
            followerService.create(5L, 2L);

            followerService.create(1L, 3L);

            //when
            ResultActions result = mockMvc.perform(get("/followers/from")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(4))
                    .andDo(document("팔로워조회 - 성공", "팔로워조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(followersParams),
                            responseFields(pageFields).andWithPrefix("content.[].", usersFields)));
        }
    }

    @Nested
    @DisplayName("팔로잉조회")
    class Followings {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            followerService.create(2L, 1L);
            followerService.create(2L, 3L);
            followerService.create(2L, 4L);
            followerService.create(2L, 5L);

            followerService.create(3L, 1L);

            //when
            ResultActions result = mockMvc.perform(get("/followers/to")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(4))
                    .andDo(document("팔로잉조회 - 성공", "팔로잉조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(followingsParams),
                            responseFields(pageFields).andWithPrefix("content.[].", usersFields)));
        }
    }

    @Nested
    @DisplayName("유저팔로워조회")
    class UserFollowers {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            followerService.create(1L, 2L);
            followerService.create(3L, 2L);
            followerService.create(4L, 2L);
            followerService.create(5L, 2L);

            followerService.create(1L, 3L);

            //when
            ResultActions result = mockMvc.perform(get("/users/{userId}/followers/from", 2L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(4))
                    .andDo(document("유저팔로워조회 - 성공", "유저팔로워조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(followersParams),
                            responseFields(pageFields).andWithPrefix("content.[].", usersFields)));
        }
    }

    @Nested
    @DisplayName("유저팔로잉조회")
    class UserFollowings {

        @Test
        @DisplayName("성공")
        public void Success() throws Exception {
            //given
            followerService.create(2L, 1L);
            followerService.create(2L, 3L);
            followerService.create(2L, 4L);
            followerService.create(2L, 5L);

            followerService.create(3L, 1L);

            //when
            ResultActions result = mockMvc.perform(get("/users/{userId}/followers/to", 2L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(4))
                    .andDo(document("유저팔로잉조회 - 성공", "유저팔로잉조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(followingsParams),
                            responseFields(pageFields).andWithPrefix("content.[].", usersFields)));
        }
    }
}