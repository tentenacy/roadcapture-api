package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.base.ErrorCode;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UserUpdateRequest;
import com.untilled.roadcapture.domain.address.Address;
import com.untilled.roadcapture.domain.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.Embedded;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiControllerTest extends ApiDocumentationTest {

    @Autowired
    private UserService userService;

    private FieldDescriptor[] signupRequestFields = new FieldDescriptor[]{
            fieldWithPath("email").description("사용자 이메일입니다. 이메일 형식이어야 합니다."),
            fieldWithPath("password").description("사용자 비밀번호입니다. 영문 숫자 조합 최소 8자 이상에서 최대 64자 이하여야 합니다."),
            fieldWithPath("username").description("사용자 이름입니다. 최소 2자 이상에서 최대 12자 이하여야 합니다.")
    };

    /*private String username;
    private String profileImageUrl;
    private String introduction;
    @Embedded
    private Address address;*/

    private FieldDescriptor[] userUpdateRequestFields = new FieldDescriptor[]{
            fieldWithPath("username").description("변경할 사용자 이름입니다. 최소 2자 이상에서 최대 12자 이하여야 합니다.").optional(),
            fieldWithPath("profileImageUrl").description("변경할 사용자 프로필 사진입니다. 주소 형식여야 합니다.").optional(),
            fieldWithPath("introduction").description("변경할 사용자 소개입니다. 최대 200자 이하여야 합니다.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("변경할 사용자 주소입니다.").optional(),
    };


    private FieldDescriptor[] badFields = new FieldDescriptor[]{
            fieldWithPath("code").description("에러 코드입니다."),
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드입니다."),
            fieldWithPath("message").description("에러 메시지입니다."),
            fieldWithPath("errors").description("필드 에러입니다. 필드 검증 시에만 존재합니다.")
    };

    private FieldDescriptor[] errorsElementsFields = new FieldDescriptor[]{
            fieldWithPath("field").description("검증에 실패한 필드명입니다."),
            fieldWithPath("value").description("검증에 실패한 필드값입니다."),
            fieldWithPath("reason").description("검증에 실패한 이유입니다.")
    };

    private FieldDescriptor[] usersElementsFields = new FieldDescriptor[]{
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional()
    };

    private FieldDescriptor[] userFields = new FieldDescriptor[]{
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("introduction").description("사용자 소개글입니다.").optional(),
    };

    private FieldDescriptor[] userDetailFields = new FieldDescriptor[]{
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("email").description("사용자 이메일입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("introduction").description("사용자 소개글입니다.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("사용자 주소입니다.").optional(),
    };

    private FieldDescriptor[] addressElementsFields = new FieldDescriptor[]{
            fieldWithPath("addressName").type(JsonFieldType.STRING).description("지번주소입니다.").optional(),
            fieldWithPath("roadAddressName").type(JsonFieldType.STRING).description("도로명주소입니다.").optional(),
            fieldWithPath("region1DepthName").type(JsonFieldType.STRING).description("시구명입니다.").optional(),
            fieldWithPath("region2DepthName").type(JsonFieldType.STRING).description("시군구명입니다.").optional(),
            fieldWithPath("region3DepthName").type(JsonFieldType.STRING).description("읍면동명입니다.").optional(),
            fieldWithPath("zoneNo").type(JsonFieldType.NUMBER).description("우편번호입니다.").optional(),
    };

    private FieldDescriptor[] placesElementsFields = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("장소 아이디입니다.").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("장소 이름입니다.").optional(),
            fieldWithPath("latitude").type(JsonFieldType.STRING).description("장소 위도입니다.").optional(),
            fieldWithPath("longitude").type(JsonFieldType.STRING).description("장소 경도입니다.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("장소 주소입니다.").optional(),
    };

    private ParameterDescriptor[] usersRequestParams = new ParameterDescriptor[]{
            parameterWithName("page").description("조회할 페이지입니다. 0부터 시작합니다.").optional(),
            parameterWithName("size").description("한 페이지에 보여줄 사이즈 수입니다.").optional(),
            parameterWithName("sort").description("정렬 기준입니다.").optional()
    };

    private ParameterDescriptor[] userPathParams = new ParameterDescriptor[]{
            parameterWithName("id").description("조회할 사용자 아이디입니다."),
    };

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
                    .andDo(document("회원조회 - 성공",
                            requestParameters(usersRequestParams),
                            responseFields().andWithPrefix("content.[].", usersElementsFields).and(usersFields)
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
            ResultActions result = mockMvc.perform(get("/users/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user1"))
                    .andDo(document("회원단건조회 - 성공",
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
                    .andDo(document("회원단건조회 - 회원 존재하지 않으면 실패",
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
            ResultActions result = mockMvc.perform(get("/users/{id}/details", 1L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user1"))
                    .andDo(document("회원상세조회 - 성공",
                            responseFields(userDetailFields).andWithPrefix("address.", addressElementsFields)));
        }

        @Test
        @DisplayName("회원 존재하지 않으면 실패")
        void UserNotFound_Fail() throws Exception {
            //when
            ResultActions result = mockMvc.perform(get("/users/{id}/details", 0L)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원상세조회 - 회원 존재하지 않으면 실패",
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
                            15073
                    )
            );

            //when
            ResultActions result = mockMvc.perform(patch("/users/{id}", 101L)
                    .content(mapper.writeValueAsString(userUpdateRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andDo(document("회원수정 - 성공",
                            requestFields(userUpdateRequestFields).andWithPrefix("address.", addressElementsFields)
                    ));
        }

        @Test
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
                            15073
                    )
            );

            //when
            ResultActions result = mockMvc.perform(patch("/users/{id}", 101L)
                    .content(mapper.writeValueAsString(userUpdateRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원수정 - 닉네임 길이가 2보다 짧으면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
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
                            15073
                    )
            );

            //when
            ResultActions result = mockMvc.perform(patch("/users/{id}", 101L)
                    .content(mapper.writeValueAsString(userUpdateRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("회원수정 - 닉네임 길이가 12보다 길면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
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
                    .andDo(document("회원가입 - 성공",
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
                    .andDo(document("회원가입 - 이메일 중복 시 실패",
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("닉네임 중복 시 실패")
        void UsernameIsDuplicated_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd1234", "user1");

            userService.signup(new SignupRequest("test2@gmail.com", "abcd1234", "test"));

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.code").value(ErrorCode.NICKNAME_EMAIL_DUPLICATION.getCode()))
                    .andDo(document("회원가입 - 닉네임 중복 시 실패",
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
                    .andDo(document("회원가입 - 이메일 형식 맞지 않으면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
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
                    .andDo(document("회원가입 - 닉네임 길이가 2보다 짧으면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
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
                    .andDo(document("회원가입 - 닉네임 길이가 12보다 길면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
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
                    .andDo(document("회원가입 - 비밀번호 형식 맞지 않으면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
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
                    .andDo(document("회원가입 - 비밀번호 길이가 8보다 짧으면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
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
                    .andDo(document("회원가입 - 비밀번호 길이가 64보다 길면 실패",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields)));
        }

    }

}