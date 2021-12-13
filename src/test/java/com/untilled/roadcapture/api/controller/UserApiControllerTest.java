package com.untilled.roadcapture.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.untilled.roadcapture.api.dto.base.ErrorCode;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.api.dto.user.UsersResponse;
import com.untilled.roadcapture.api.exception.EmailDuplicatedException;
import com.untilled.roadcapture.api.exception.UsernameDuplicatedException;
import com.untilled.roadcapture.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class UserApiControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private FieldDescriptor[] signupRequestFields = new FieldDescriptor[]{
            fieldWithPath("email").description("사용자 이메일입니다. 이메일 형식이어야 합니다."),
            fieldWithPath("password").description("사용자 비밀번호입니다. 영문 숫자 조합 최소 8자 이상에서 최대 64자 이하여야 합니다."),
            fieldWithPath("username").description("사용자 이름입니다. 최소 2자 이상에서 최대 12자 이하여야 합니다.")
    };

    private FieldDescriptor[] badCommon = new FieldDescriptor[]{
            fieldWithPath("code").description("에러 코드입니다."),
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드입니다."),
            fieldWithPath("message").description("에러 메시지입니다."),
            fieldWithPath("errors").description("필드 에러입니다. 필드 검증 시에만 존재합니다.")
    };

    private FieldDescriptor[] badCommonErrors = new FieldDescriptor[]{
            fieldWithPath("errors.[].field").description("검증에 실패한 필드명입니다."),
            fieldWithPath("errors.[].value").description("검증에 실패한 필드값입니다."),
            fieldWithPath("errors.[].reason").description("검증에 실패한 이유입니다.")
    };

    private FieldDescriptor[] okPage = new FieldDescriptor[]{
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("조회된 사용자 리스트입니다."),
            fieldWithPath("pageable").description(""),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description(""),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description(""),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description(""),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description(""),
            fieldWithPath("number").type(JsonFieldType.NUMBER).description(""),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description(""),
            fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description(""),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description(""),
            subsectionWithPath("sort").type(JsonFieldType.OBJECT).description(""),
    };

    private FieldDescriptor[] okPageContent = new FieldDescriptor[]{
            fieldWithPath("content.[].id").description("조회된 사용자 아이디입니다."),
            fieldWithPath("content.[].username").description("조회된 사용자 이름입니다."),
            fieldWithPath("content.[].profileImageUrl").description("조회된 사용자 프로필 사진입니다.")
    };

    private ParameterDescriptor[] pageParam = new ParameterDescriptor[]{
            parameterWithName("page").description("조회할 페이지입니다. 기본값은 1입니다.").optional(),
            parameterWithName("size").description("한 페이지에 조회할 데이터 수입니다. 기본값은 10입니다.").optional(),
            parameterWithName("sort").description("정렬 방향입니다. 기본값은 오름차순입니다.").optional()
    };

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("회원가입")
    class Signup {
        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abdc1234", "user");

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
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd1234", "user2");

            willThrow(new EmailDuplicatedException()).given(userService).signup(any());

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_DUPLICATION.getCode()))
                    .andDo(document("users-signup-email-duplicate",
                            responseFields(badCommon)));
        }

        @Test
        @DisplayName("닉네임 중복 시 실패")
        void UsernameIsDuplicated_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test2@gmail.com", "abcd1234", "user");

            willThrow(new UsernameDuplicatedException()).given(userService).signup(any());

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.NICKNAME_EMAIL_DUPLICATION.getCode()))
                    .andDo(document("users-signup-username-duplicate"));
        }

        @Test
        @DisplayName("이메일 형식 맞지 않으면 실패")
        void EmailTypeIsMismatched_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test", "abdc1234", "user");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-email-mismatch",
                            responseFields(badCommonErrors).and(badCommon)));
        }

        @Test
        @DisplayName("닉네임 길이가 2보다 짧으면 실패")
        void UsernameSizeIsLessThan2_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abdc1234", "u");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-username-shorter"));
        }

        @Test
        @DisplayName("닉네임 길이가 12보다 길면 실패")
        void UsernameSizeIsGreaterThan12_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abdc1234", "usernameusern");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-username-longer"));
        }

        @Test
        @DisplayName("비밀번호 형식 맞지 않으면 실패")
        void PasswordTypeIsMismatch_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcdefgh", "username");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-password-mismatch"));
        }

        @Test
        @DisplayName("비밀번호 길이가 8보다 짧으면 실패")
        void PasswordSizeIsLessThan8_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcd123", "username");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-password-shorter"));
        }

        @Test
        @DisplayName("비밀번호 길이가 64보다 길면 실패")
        void PasswordSizeIsGreaterThan64_Fail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test@gmail.com", "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789", "username");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("users-signup-password-longer"));
        }

    }

    @Nested
    @DisplayName("유저 조회")
    class Users {
        @Test
        @DisplayName("성공")
        void Success() throws Exception {
            //given
            Page<UsersResponse> usersResponses = new PageImpl<>(Arrays.asList(
                    new UsersResponse(1L, "user", "http://www.test.com/images/1"),
                    new UsersResponse(2L, "user2", "http://www.test.com/images/2")
            ));
            given(userService.findUsers(any())).willReturn(usersResponses);

            //when
            ResultActions result = mockMvc.perform(get("/users")
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(1L))
                    .andExpect(jsonPath("$.content[1].id").value(2L))
                    .andDo(document("users",
                            requestParameters(pageParam),
                            responseFields(okPageContent).and(okPage)
                    ));
        }
    }

}