package com.untilled.roadcapture.api.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.untilled.roadcapture.api.controller.UserApiController;
import com.untilled.roadcapture.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
public abstract class ApiDocumentationTest {

    protected final ObjectMapper mapper = new ObjectMapper();

    protected FieldDescriptor[] okPageResponseFields = new FieldDescriptor[]{
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("페이지 요소 리스트입니다."),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부입니다."),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수입니다."),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수입니다."),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("한 페이지에 보여줄 사이즈 수입니다."),
            fieldWithPath("number").ignored(),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부입니다."),
            fieldWithPath("numberOfElements").ignored(),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어 있는지 여부입니다."),
            subsectionWithPath("sort").ignored(),
            subsectionWithPath("pageable").ignored(),
    };

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserService userService;
}
