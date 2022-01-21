package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.dto.token.TokenRequest;
import com.untilled.roadcapture.api.dto.user.*;
import com.untilled.roadcapture.domain.address.Address;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
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

class UserApiControllerTest extends ApiDocumentationTest {

    @Nested
    @DisplayName("조회")
    class Users {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users")
                    .queryParam("sort", "createdAt,asc")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].username").value("유저1"))
                    .andExpect(jsonPath("$.content[1].username").value("유저2"))
                    .andDo(document("회원조회 - 성공", "회원조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(usersParams),
                            responseFields().andWithPrefix("content.[].", usersFields).and(pageFields)
                    ));
        }

        @Test
        @DisplayName("회원 이름으로 검색 성공")
        void SearchByUsername_Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users")
                    .queryParam("sort", "createdAt,desc")
                    .queryParam("username", "유저2")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].username").value("유저2"))
                    .andDo(document("회원조회 - 유저 이름으로 검색 성공", "회원조회",
                            requestHeaders(jwtHeader),
                            requestParameters(pageParams).and(usersParams),
                            responseFields().andWithPrefix("content.[].", usersFields).and(pageFields)
                    ));
        }
    }

    @Nested
    @DisplayName("스튜디오회원조회")
    class StudioUser {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/{userId}", 2L)
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("유저2"))
                    .andDo(document("스튜디오회원조회 - 성공", "스튜디오회원조회",
                            requestHeaders(jwtHeader),
                            responseFields(studioUserFields)));
        }
    }

    @Nested
    @DisplayName("마이스튜디오회원조회")
    class MyStudioUser {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/me")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("유저2"))
                    .andDo(document("마이스튜디오회원조회 - 성공", "마이스튜디오회원조회",
                            requestHeaders(jwtHeader),
                            responseFields(myStudioUserFields)));
        }
    }

    @Nested
    @DisplayName("상세조회")
    class UserDetail {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/details")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("유저2"))
                    .andDo(document("회원상세조회 - 성공", "회원상세조회",
                            requestHeaders(jwtHeader),
                            responseFields(userDetailFields).andWithPrefix("address.", addressFields)));
        }
    }

    @Nested
    @DisplayName("수정")
    class Update {
        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                    "updated101",
                    "https://test.com/updatedTest",
                    "https://usercontents-d.styleshare.io/images/46615561/1280x-",
                    "안녕하세요. 저는 updated101입니다.",
                    new Address("경기 시흥시 정왕동 2121-3 경기과학기술대학",
                            "경기 시흥시 경기과기대로 269 경기과학기술대학",
                            "경기도",
                            "시흥시",
                            "정왕동",
                            "15073"
                    )
            );

            //when
            ResultActions result = mockMvc.perform(patch("/users")
                    .content(mapper.writeValueAsString(userUpdateRequest))
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("회원수정 - 성공", "회원수정",
                            requestHeaders(jwtHeader),
                            requestFields(userUpdateRequestFields).andWithPrefix("address.", addressFields)
                    ));
        }
    }

    @Nested
    @DisplayName("삭제")
    class Delete {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(delete("/users")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("회원삭제 - 성공", "회원삭제",
                            requestHeaders(jwtHeader)));
        }

        @Test
        @DisplayName("유저 삭제하고 리프레시토큰 삭제되면 성공")
        void RefreshTokenDeletedAsUserDeleted_Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(delete("/users")
                    .header("X-AUTH-TOKEN", jwtAccessToken)
                    .contentType(MediaType.APPLICATION_JSON));


            //then
            Assertions.assertThat(refreshTokenRepository.findByKey(2L)).isEmpty();
            result.andExpect(status().isNoContent())
                    .andDo(document("회원삭제 - 유저 삭제하고 리프레시토큰 삭제되면 성공", "회원삭제",
                            requestHeaders(jwtHeader)));
        }
    }

    @Nested
    @DisplayName("회원가입")
    class Signup {
        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd1234", "test");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("회원가입 - 성공", "회원가입",
                            requestFields(signupRequestFields)
                    ));
        }

        @Test
        @DisplayName("기가입 시 실패")
        void AlreadySignedup_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("user1@gmail.com", "abcd1234", "test");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.ALREADY_SIGNEDUP.getCode()))
                    .andDo(document("회원가입 - 기가입 시 실패", "회원가입",
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("이메일 형식 맞지 않으면 실패")
        void EmailTypeIsMismatched_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test", "abcd1234", "test");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원가입 - 이메일 형식 맞지 않으면 실패", "회원가입",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }

        @Test
        @DisplayName("닉네임 길이가 2보다 짧으면 실패")
        void UsernameSizeIsLessThan2_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd1234", "t");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원가입 - 닉네임 길이가 2보다 짧으면 실패", "회원가입",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }

        @Test
        @DisplayName("닉네임 길이가 12보다 길면 실패")
        void UsernameSizeIsGreaterThan12_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd1234", "testtesttesttest");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원가입 - 닉네임 길이가 12보다 길면 실패", "회원가입",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }

        @Test
        @DisplayName("비밀번호 형식 맞지 않으면 실패")
        void PasswordTypeIsMismatch_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcdefgh", "test");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원가입 - 비밀번호 형식 맞지 않으면 실패", "회원가입",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }

        @Test
        @DisplayName("비밀번호 길이가 8보다 짧으면 실패")
        void PasswordSizeIsLessThan8_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd123", "test");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원가입 - 비밀번호 길이가 8보다 짧으면 실패", "회원가입",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }

        @Test
        @DisplayName("비밀번호 길이가 64보다 길면 실패")
        void PasswordSizeIsGreaterThan64_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789", "test");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원가입 - 비밀번호 길이가 64보다 길면 실패", "회원가입",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }

    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/token")
                    .content(mapper.writeValueAsString(new LoginRequest("user1@gmail.com", "abcd1234")))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("로그인 - 성공", "로그인",
                            requestFields(loginRequestFields),
                            responseFields(tokenFields)));
        }
    }

    @Nested
    @DisplayName("토큰재발급")
    class TokenReissue {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(post("/users/token/reissue")
                    .content(mapper.writeValueAsString(new TokenRequest(jwtAccessToken, jwtRefreshToken)))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("토큰재발급 - 성공", "토큰재발급",
                            requestFields(tokenRequestFields),
                            responseFields(tokenFields)));
        }
    }

}