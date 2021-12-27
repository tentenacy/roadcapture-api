package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.base.ErrorCode;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UserUpdateRequest;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiControllerTest extends ApiDocumentationTest {

    private UserService userService;

    @Nested
    @DisplayName("조회")
    class Users {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users")
                    .queryParam("sort", "id,asc")
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].username").value("user1"))
                    .andExpect(jsonPath("$.content[1].username").value("user2"))
                    .andDo(document("회원조회 - 성공", "회원조회",
                            requestParameters(pageParams),
                            responseFields().andWithPrefix("content.[].", usersElementsFields).and(pageFields)
                    ));
        }
    }

    @Nested
    @DisplayName("단건조회")
    class User {
        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/{id}", 2L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user2"))
                    .andDo(document("회원단건조회 - 성공", "회원단건조회",
                            pathParameters(userPathParams),
                            responseFields(userFields)));
        }

        @Test
        @DisplayName("회원 존재하지 않으면 실패")
        void UserNotFound_Fail() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/{id}", 0L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원단건조회 - 회원 존재하지 않으면 실패", "회원단건조회",
                            pathParameters(userPathParams),
                            responseFields(badFields)));
        }
    }

    @Nested
    @DisplayName("상세조회")
    class UserDetail {

        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/{id}/details", 2L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user2"))
                    .andDo(document("회원상세조회 - 성공", "회원상세조회",
                            responseFields(userDetailFields).andWithPrefix("address.", addressFields)));
        }

        @Test
        @DisplayName("회원 존재하지 않으면 실패")
        void UserNotFound_Fail() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/{id}/details", 0L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원상세조회 - 회원 존재하지 않으면 실패", "회원상세조회",
                            pathParameters(userPathParams),
                            responseFields(badFields)));
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
            ResultActions result = mockMvc.perform(patch("/users/{id}", 2L)
                    .content(mapper.writeValueAsString(userUpdateRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("회원수정 - 성공", "회원수정",
                            requestFields(userUpdateRequestFields).andWithPrefix("address.", addressFields)
                    ));
        }

        /*@Test
        @DisplayName("닉네임 길이가 2보다 짧으면 실패")
        void UsernameSizeIsLessThan2_Fail() throws Exception {
            //given
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                    "u",
                    "https://test.com/updatedTest",
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
            ResultActions result = mockMvc.perform(patch("/users/{id}", 2L)
                    .content(mapper.writeValueAsString(userUpdateRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원수정 - 닉네임 길이가 2보다 짧으면 실패", "회원수정",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }

        @Test
        @DisplayName("닉네임 길이가 12보다 길면 실패")
        void UsernameSizeIsGreaterThan12_Fail() throws Exception {
            //given
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                    "updated101updated101",
                    "https://test.com/updatedTest",
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
            ResultActions result = mockMvc.perform(patch("/users/{id}", 2L)
                    .content(mapper.writeValueAsString(userUpdateRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원수정 - 닉네임 길이가 12보다 길면 실패", "회원수정",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsFields)));
        }*/
    }

    @Nested
    @DisplayName("삭제")
    class Delete {
        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given

            //when
            ResultActions result = mockMvc.perform(delete("/users/{id}", 2L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isNoContent())
                    .andDo(document("회원삭제 - 성공", "회원삭제"));
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
        @DisplayName("이메일 중복 시 실패")
        void EmailIsDuplicated_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("user1@gmail.com", "abcd1234", "test");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_DUPLICATION.getCode()))
                    .andDo(document("회원가입 - 이메일 중복 시 실패", "회원가입",
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("닉네임 중복 시 실패")
        void UsernameIsDuplicated_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd1234", "user1");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.code").value(ErrorCode.NICKNAME_EMAIL_DUPLICATION.getCode()))
                    .andDo(document("회원가입 - 닉네임 중복 시 실패", "회원가입",
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

}