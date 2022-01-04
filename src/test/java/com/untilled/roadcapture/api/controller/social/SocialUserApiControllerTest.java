package com.untilled.roadcapture.api.controller.social;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.dto.user.SocialLoginRequest;
import com.untilled.roadcapture.api.dto.user.SocialSignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SocialUserApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("카카오회원가입")
    class SignupByKakao {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/kakao")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(kakaoOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("카카오회원가입 - 성공", "카카오회원가입",
                            requestFields(socialRequestFields)
                    ));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/kakao")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(kakaoOAuth2AccessToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("카카오회원가입 - 토큰 유효하지 않으면 실패", "카카오회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }

        @Test
        @DisplayName("기가입 시 실패")
        void AlreadySignedup_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/kakao")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(kakaoOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/kakao")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(kakaoOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.ALREADY_SIGNEDUP.getCode()))
                    .andDo(document("카카오회원가입 - 기가입 시 실패", "카카오회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }
    }

    @Nested
    @DisplayName("구글회원가입")
    class SignupByGoogle {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/google")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(googleOAuth2IdToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("구글회원가입 - 성공", "구글회원가입",
                            requestFields(socialRequestFields)
                    ));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/google")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(googleOAuth2IdToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("구글회원가입 - 토큰 유효하지 않으면 실패", "구글회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }

        @Test
        @DisplayName("기가입 시 실패")
        void AlreadySignedup_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/google")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(googleOAuth2IdToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/google")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(googleOAuth2IdToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.ALREADY_SIGNEDUP.getCode()))
                    .andDo(document("구글회원가입 - 기가입 시 실패", "구글회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }
    }

    @Nested
    @DisplayName("네이버회원가입")
    class SignupByNaver {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/naver")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(naverOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("네이버회원가입 - 성공", "네이버회원가입",
                            requestFields(socialRequestFields)
                    ));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/naver")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(naverOAuth2AccessToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("네이버회원가입 - 토큰 유효하지 않으면 실패", "네이버회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }

        @Test
        @DisplayName("기가입 시 실패")
        void AlreadySignedup_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/naver")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(naverOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/naver")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(naverOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.ALREADY_SIGNEDUP.getCode()))
                    .andDo(document("네이버회원가입 - 기가입 시 실패", "네이버회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }
    }

    @Nested
    @DisplayName("페이스북회원가입")
    class SignupByFacebook {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/facebook")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(facebookOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("페이스북회원가입 - 성공", "페이스북회원가입",
                            requestFields(socialRequestFields)
                    ));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/facebook")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(facebookOAuth2AccessToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("페이스북회원가입 - 토큰 유효하지 않으면 실패", "페이스북회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }

        @Test
        @DisplayName("기가입 시 실패")
        void AlreadySignedup_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/facebook")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(facebookOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/facebook")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(facebookOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.ALREADY_SIGNEDUP.getCode()))
                    .andDo(document("페이스북회원가입 - 기가입 시 실패", "페이스북회원가입",
                            requestFields(socialRequestFields),
                            responseFields(badFields)
                    ));
        }
    }

    @Nested
    @DisplayName("카카오로그인")
    class LoginByKakao {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            mockMvc.perform(post("/users/social/kakao")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(kakaoOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when

            ResultActions result = mockMvc.perform(post("/users/social/kakao/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(kakaoOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("카카오로그인 - 성공", "카카오로그인",
                            requestFields(socialRequestFields),
                            responseFields(tokenFields)));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/kakao")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(kakaoOAuth2AccessToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/kakao/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest("accessToken_invaild")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("카카오로그인 - 토큰 유효하지 않으면 실패", "카카오로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("미가입 시 실패")
        void SignedupYet_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/kakao/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(kakaoOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
                    .andDo(document("카카오로그인 - 미가입 시 실패", "카카오로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }
    }

    @Nested
    @DisplayName("구글로그인")
    class LoginByGoogle {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            mockMvc.perform(post("/users/social/google")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(googleOAuth2IdToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/google/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(googleOAuth2IdToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("구글로그인 - 성공", "구글로그인",
                            requestFields(socialRequestFields),
                            responseFields(tokenFields)));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/google")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(googleOAuth2IdToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/google/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest("accessToken_invaild")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("구글로그인 - 토큰 유효하지 않으면 실패", "구글로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("미가입 시 실패")
        void SignedupYet_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/google/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(googleOAuth2IdToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
                    .andDo(document("구글로그인 - 미가입 시 실패", "구글로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }
    }

    @Nested
    @DisplayName("네이버로그인")
    class LoginByNaver {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            mockMvc.perform(post("/users/social/naver")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(naverOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/naver/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(naverOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("네이버로그인 - 성공", "네이버로그인",
                            requestFields(socialRequestFields),
                            responseFields(tokenFields)));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/naver")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(naverOAuth2AccessToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/naver/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest("accessToken_invaild")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("네이버로그인 - 토큰 유효하지 않으면 실패", "네이버로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("미가입 시 실패")
        void SignedupYet_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/naver/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(naverOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
                    .andDo(document("네이버로그인 - 미가입 시 실패", "네이버로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }
    }

    @Nested
    @DisplayName("페이스북로그인")
    class LoginByFacebook {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            mockMvc.perform(post("/users/social/facebook")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(facebookOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/facebook/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(facebookOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("페이스북로그인 - 성공", "페이스북로그인",
                            requestFields(socialRequestFields),
                            responseFields(tokenFields)));
        }

        @Test
        @DisplayName("토큰 유효하지 않으면 실패")
        void TokenIsInvalid_Fail() throws Exception {
            //given
            mockMvc.perform(post("/users/social/facebook")
                    .content(mapper.writeValueAsString(new SocialSignupRequest(facebookOAuth2AccessToken + "_invalid")))
                    .contentType(MediaType.APPLICATION_JSON));

            //when
            ResultActions result = mockMvc.perform(post("/users/social/facebook/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest("accessToken_invaild")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.SOCIAL_COMMUNICATION_ERROR.getCode()))
                    .andDo(document("페이스북로그인 - 토큰 유효하지 않으면 실패", "페이스북로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("미가입 시 실패")
        void SignedupYet_Fail() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/social/facebook/token")
                    .content(mapper.writeValueAsString(new SocialLoginRequest(facebookOAuth2AccessToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
                    .andDo(document("페이스북로그인 - 미가입 시 실패", "페이스북로그인",
                            requestFields(socialRequestFields),
                            responseFields(badFields)));
        }
    }
}
