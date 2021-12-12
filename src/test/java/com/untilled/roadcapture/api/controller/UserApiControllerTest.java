package com.untilled.roadcapture.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.untilled.roadcapture.api.dto.user.SignupRequest;
import com.untilled.roadcapture.domain.user.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("회원가입")
    class Signup {
        @Test
        @DisplayName("이메일 형식 맞지 않으면 실패")
        void When_EmailTypeIsMismatched_Expect_SignupToFail() throws Exception {
            //given
            final SignupRequest signupRequest = new SignupRequest("test", "abdc1234", "tester");

            //when
            ResultActions result = mockMvc.perform(post("/users")
                    .content(mapper.writeValueAsString(signupRequest))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            result.andExpect(status().isBadRequest())
                    .andDo(document("user-signup-email-mismatch",
                            requestFields(
                                    fieldWithPath("email").description("사용자 이메일입니다. 이메일 형식이어야 합니다."),
                                    fieldWithPath("password").description("사용자 비밀번호입니다. 영문 숫자 조합 최소 8자 이상이어야 합니다."),
                                    fieldWithPath("username").description("사용자 이름입니다. 최소 2자, 최대 12자입니다.")
                            )
                    ));
        }
    }
}