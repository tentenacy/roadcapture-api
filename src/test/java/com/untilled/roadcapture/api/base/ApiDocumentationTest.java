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
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
public abstract class ApiDocumentationTest {

    protected final ObjectMapper mapper = new ObjectMapper();

    protected FieldDescriptor[] usersElementsFields = new FieldDescriptor[]{
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional()
    };

    protected FieldDescriptor[] userFields = new FieldDescriptor[]{
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("introduction").description("사용자 소개글입니다.").optional(),
    };

    protected FieldDescriptor[] userDetailFields = new FieldDescriptor[]{
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("email").description("사용자 이메일입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("introduction").description("사용자 소개글입니다.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("사용자 주소입니다.").optional(),
    };

    protected FieldDescriptor[] albumCreateRequestFields = new FieldDescriptor[]{
            fieldWithPath("title").description("앨범 제목입니다."),
            fieldWithPath("description").description("앨범 설명입니다."),
            fieldWithPath("thumbnailUrl").description("앨범 썸네일 이미지 주소입니다."),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("앨범 사진 리스트입니다."),
            fieldWithPath("userId").description("앨범 등록자 아이디입니다."),
    };

    protected FieldDescriptor[] albumFields = new FieldDescriptor[]{
            fieldWithPath("id").description("앨범 아이디입니다."),
            fieldWithPath("createdAt").description("앨범 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").description("앨범 수정 시각입니다."),
            fieldWithPath("title").description("앨범 제목입니다."),
            fieldWithPath("description").description("앨범 설명입니다."),
            fieldWithPath("thumbnailUrl").description("앨범 썸네일 이미지 주소입니다."),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("앨범 조회수입니다."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("앨범 등록자입니다."),
            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("앨범 좋아요수입니다."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("앨범 댓글수입니다."),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("앨범 사진 리스트입니다."),
    };

    protected FieldDescriptor[] albumsFields = new FieldDescriptor[]{
            fieldWithPath("id").description("앨범 아이디입니다."),
            fieldWithPath("createdAt").description("앨범 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").description("앨범 수정 시각입니다."),
            fieldWithPath("title").description("앨범 제목입니다."),
            fieldWithPath("description").description("앨범 설명입니다."),
            fieldWithPath("thumbnailUrl").description("앨범 썸네일 이미지 주소입니다."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("앨범 등록자입니다."),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("앨범 조회수입니다."),
            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("앨범 좋아요수입니다."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("앨범 댓글수입니다."),
    };


    protected FieldDescriptor[] pictureFields = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("사진 아이디입니다."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사진 생성 시각입니다."),
            fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("사진 수정 시각입니다."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("사진 이미지 주소입니다."),
            fieldWithPath("description").type(JsonFieldType.STRING).description("사진 설명입니다."),
            fieldWithPath("place").type(JsonFieldType.OBJECT).description("사진 장소입니다."),
    };

    protected FieldDescriptor[] placeFields = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("장소 아이디입니다."),
            fieldWithPath("name").type(JsonFieldType.STRING).description("장소 이름입니다."),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("장소 위도입니다."),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("장소 경도입니다."),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("장소 주소입니다."),
    };

    protected FieldDescriptor[] pictureCreateRequestFields = new FieldDescriptor[]{
            fieldWithPath("imageUrl").description("사진 이미지 주소입니다."),
            fieldWithPath("description").description("사진 설명입니다."),
            fieldWithPath("place").description("사진 장소입니다."),
    };

    protected FieldDescriptor[] addressFields = new FieldDescriptor[]{
            fieldWithPath("addressName").type(JsonFieldType.STRING).description("지번주소입니다.").optional(),
            fieldWithPath("roadAddressName").type(JsonFieldType.STRING).description("도로명주소입니다.").optional(),
            fieldWithPath("region1DepthName").type(JsonFieldType.STRING).description("시구명입니다.").optional(),
            fieldWithPath("region2DepthName").type(JsonFieldType.STRING).description("시군구명입니다.").optional(),
            fieldWithPath("region3DepthName").type(JsonFieldType.STRING).description("읍면동명입니다.").optional(),
            fieldWithPath("zoneNo").type(JsonFieldType.STRING).description("우편번호입니다.").optional(),
    };

    protected FieldDescriptor[] placeCreateRequestFields = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("장소 아이디입니다.").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("장소 이름입니다.").optional(),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("장소 위도입니다.").optional(),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("장소 경도입니다.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("장소 주소입니다.").optional(),
    };

    protected ParameterDescriptor[] pageParams = new ParameterDescriptor[]{
            parameterWithName("page").description("조회할 페이지입니다. 0부터 시작합니다.").optional(),
            parameterWithName("size").description("한 페이지에 보여줄 사이즈 수입니다.").optional(),
            parameterWithName("sort").description("정렬 기준입니다.").optional()
    };

    protected FieldDescriptor[] pageFields = new FieldDescriptor[]{
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("페이지 요소 리스트입니다."),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부입니다."),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수입니다."),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수입니다."),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("한 페이지에 보여줄 사이즈 수입니다."),
            fieldWithPath("number").type(JsonFieldType.NUMBER).ignored(),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부입니다."),
            fieldWithPath("numberOfElements").ignored(),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("리스트가 비어 있는지 여부입니다."),
            subsectionWithPath("sort").type(JsonFieldType.OBJECT).ignored(),
            subsectionWithPath("pageable").type(JsonFieldType.OBJECT).ignored(),
    };

    protected FieldDescriptor[] badFields = new FieldDescriptor[]{
            fieldWithPath("code").description("에러 코드입니다."),
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드입니다."),
            fieldWithPath("message").description("에러 메시지입니다."),
            fieldWithPath("errors").description("필드 에러입니다. 필드 검증 시에만 존재합니다.")
    };

    protected FieldDescriptor[] errorsFields = new FieldDescriptor[]{
            fieldWithPath("field").description("검증에 실패한 필드명입니다."),
            fieldWithPath("value").description("검증에 실패한 필드값입니다."),
            fieldWithPath("reason").description("검증에 실패한 이유입니다.")
    };

    @Autowired
    protected MockMvc mockMvc;
}
