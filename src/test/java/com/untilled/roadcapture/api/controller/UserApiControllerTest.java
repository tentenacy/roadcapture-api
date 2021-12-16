package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.base.ApiDocumentationTest;
import com.untilled.roadcapture.api.dto.base.ErrorCode;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.domain.user.User;
import com.untilled.roadcapture.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserApiControllerTest extends ApiDocumentationTest {

    @SpyBean
    private UserService userService;

    private FieldDescriptor[] signupRequestFields = new FieldDescriptor[]{
            fieldWithPath("email").description("사용자 이메일입니다. 이메일 형식이어야 합니다."),
            fieldWithPath("password").description("사용자 비밀번호입니다. 영문 숫자 조합 최소 8자 이상에서 최대 64자 이하여야 합니다."),
            fieldWithPath("username").description("사용자 이름입니다. 최소 2자 이상에서 최대 12자 이하여야 합니다.")
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
            fieldWithPath("id").description("조회된 사용자 아이디입니다."),
            fieldWithPath("username").description("조회된 사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("조회된 사용자 프로필 사진입니다.")
    };

    private FieldDescriptor[] userFields = new FieldDescriptor[]{
            fieldWithPath("id").description("조회된 사용자 아이디입니다."),
            fieldWithPath("username").description("조회된 사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("조회된 사용자 프로필 사진입니다."),
            fieldWithPath("introduction").description("조회된 사용자 프로필 사진입니다."),
            fieldWithPath("preferencePlaces").type(JsonFieldType.ARRAY).description("조회된 사용자 프로필 사진입니다."),
    };

    private FieldDescriptor[] preferencePlacesElementsFields = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("관심지역 아이디입니다.").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("관심지역 이름입니다.").optional(),
            fieldWithPath("latitude").type(JsonFieldType.STRING).description("관심지역 위도입니다.").optional(),
            fieldWithPath("longitude").type(JsonFieldType.STRING).description("관심지역 경도입니다.").optional(),
            fieldWithPath("addressName").type(JsonFieldType.STRING).description("관심지역 지번주소입니다.").optional(),
            fieldWithPath("roadAddressName").type(JsonFieldType.STRING).description("관심지역 도로명주소입니다.").optional(),
            fieldWithPath("region1DepthName").type(JsonFieldType.STRING).description("관심지역 시구명입니다.").optional(),
            fieldWithPath("region2DepthName").type(JsonFieldType.STRING).description("관심지역 시군구명입니다.").optional(),
            fieldWithPath("region3DepthName").type(JsonFieldType.STRING).description("관심지역 읍면동명입니다.").optional(),
            fieldWithPath("zoneNo").type(JsonFieldType.STRING).description("관심지역 우편번호입니다.").optional(),
    };

    private ParameterDescriptor[] usersRequestParams = new ParameterDescriptor[]{
            parameterWithName("page").description("조회할 페이지입니다. 0부터 시작합니다.").optional(),
            parameterWithName("size").description("한 페이지에 조회할 데이터 수입니다.").optional(),
            parameterWithName("sort").description("정렬 기준입니다.").optional()

    };

    @Test
    @DisplayName("조회")
    void Users_Success() throws Exception {
        //given
        willReturn(new PageImpl<>(Arrays.asList(
                User.create("users1@gmail.com", "abcd1234", "users1"),
                User.create("users2@gmail.com", "abcd1234", "users2")
        ))).willCallRealMethod().given(userService).getUsers(any());

        //when
        ResultActions result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("users1"))
                .andExpect(jsonPath("$.content[1].username").value("users2"))
                .andDo(document("users",
                        requestParameters(usersRequestParams),
                        responseFields().andWithPrefix("content.[].", usersElementsFields).and(usersFields)
                ));
    }

    @Test
    @DisplayName("단건조회")
    void User_Success() throws Exception {
        //given
        willReturn(User.create("users1@gmail.com", "abcd1234", "users1"))
                .willCallRealMethod().given(userService).getUser(any());

        //when
        ResultActions result = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("users1"))
                .andDo(document("user",
                        responseFields(userFields).andWithPrefix("preferencePlaces.[].", preferencePlacesElementsFields)));
    }

    @Nested
    @DisplayName("회원가입")
    class Signup {
        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcd1234", "signup1");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isCreated())
                    .andDo(document("users-signup",
                            requestFields(signupRequestFields)
                    ));
        }

        @Test
        @DisplayName("이메일 중복 시 실패")
        void EmailIsDuplicated_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcd1234", "signup1");

            userService.signup(new SignupRequest("signup1@gmail.com", "abcd1234", "signup2"));

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_DUPLICATION.getCode()))
                    .andDo(document("users-signup-email-duplicate",
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("닉네임 중복 시 실패")
        void UsernameIsDuplicated_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcd1234", "signup1");

            userService.signup(new SignupRequest("signup2@gmail.com", "abcd1234", "signup1"));

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())

                    .andExpect(jsonPath("$.code").value(ErrorCode.NICKNAME_EMAIL_DUPLICATION.getCode()))
                    .andDo(document("users-signup-username-duplicate",
                            responseFields(badFields)));
        }

        @Test
        @DisplayName("이메일 형식 맞지 않으면 실패")
        void EmailTypeIsMismatched_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1", "abcd1234", "signup1");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-email-mismatch",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields).and(badFields)));
        }

        @Test
        @DisplayName("닉네임 길이가 2보다 짧으면 실패")
        void UsernameSizeIsLessThan2_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcd1234", "s");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-username-shorter",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields).and(badFields)));
        }

        @Test
        @DisplayName("닉네임 길이가 12보다 길면 실패")
        void UsernameSizeIsGreaterThan12_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcd1234", "signup1signup1");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-username-longer",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields).and(badFields)));
        }

        @Test
        @DisplayName("비밀번호 형식 맞지 않으면 실패")
        void PasswordTypeIsMismatch_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcdefgh", "signup1");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-password-mismatch",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields).and(badFields)));
        }

        @Test
        @DisplayName("비밀번호 길이가 8보다 짧으면 실패")
        void PasswordSizeIsLessThan8_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcd123", "signup1");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-password-shorter",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields).and(badFields)));
        }

        @Test
        @DisplayName("비밀번호 길이가 64보다 길면 실패")
        void PasswordSizeIsGreaterThan64_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("signup1@gmail.com", "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789", "signup1");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-password-longer",
                            responseFields(badFields).andWithPrefix("errors.[].", errorsElementsFields).and(badFields)));
        }

    }

}