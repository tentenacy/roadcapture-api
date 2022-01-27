package com.untilled.roadcapture.api.base;

import com.untilled.roadcapture.api.dto.token.TokenResponse;
import com.untilled.roadcapture.api.dto.user.LoginRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestPartDescriptor;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;

@AutoConfigureRestDocs
public abstract class ApiDocumentationTest extends BaseSpringBootTest {

    protected static String kakaoOAuth2AccessToken;
    protected static String googleOAuth2AccessToken;
    protected static String googleOAuth2IdToken;
    protected static String naverOAuth2AccessToken;
    protected static String facebookOAuth2AccessToken;
    protected static String jwtAccessToken;
    protected static String jwtRefreshToken;

    @BeforeAll
    public void setup() {
        kakaoOAuth2AccessToken = env.getProperty("social.kakao.accessToken");
        googleOAuth2AccessToken = env.getProperty("social.google.accessToken");
        googleOAuth2IdToken = env.getProperty("social.google.idToken");
        naverOAuth2AccessToken = env.getProperty("social.naver.accessToken");
        facebookOAuth2AccessToken = env.getProperty("social.facebook.accessToken");

        TokenResponse token = userService.login(new LoginRequest("user2@gmail.com", "abcd1234"));
        jwtAccessToken = token.getAccessToken();
        jwtRefreshToken = token.getRefreshToken();
    }

    @AfterAll
    public void teardown() {
        refreshTokenRepository.deleteByKey(2L);
    }

    //COMMON_DESC
    protected ParameterDescriptor[] pageParams = new ParameterDescriptor[] {
            parameterWithName("page").description("조회할 페이지입니다. 0부터 시작합니다.").optional(),
            parameterWithName("size").description("한 페이지에 보여줄 사이즈 수입니다.").optional(),
            parameterWithName("sort").description("정렬 기준입니다.").optional()
    };

    protected FieldDescriptor[] pageFields = new FieldDescriptor[] {
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

    protected FieldDescriptor[] badFields = new FieldDescriptor[] {
            fieldWithPath("code").description("에러 코드입니다."),
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드입니다."),
            fieldWithPath("message").description("에러 메시지입니다."),
            fieldWithPath("errors").description("필드 에러입니다. 필드 검증 시에만 존재합니다.")
    };

    protected FieldDescriptor[] errorsFields = new FieldDescriptor[] {
            fieldWithPath("field").description("검증에 실패한 필드명입니다."),
            fieldWithPath("value").description("검증에 실패한 필드값입니다."),
            fieldWithPath("reason").description("검증에 실패한 이유입니다.")
    };

    //VALUE_DESC
    protected FieldDescriptor[] addressFields = new FieldDescriptor[] {
            fieldWithPath("addressName").type(JsonFieldType.STRING).description("지번주소입니다."),
            fieldWithPath("roadAddressName").type(JsonFieldType.STRING).description("도로명주소입니다.").optional(),
            fieldWithPath("region1DepthName").type(JsonFieldType.STRING).description("시도명입니다."),
            fieldWithPath("region2DepthName").type(JsonFieldType.STRING).description("시군구명입니다."),
            fieldWithPath("region3DepthName").type(JsonFieldType.STRING).description("읍면동명입니다."),
            fieldWithPath("zoneNo").type(JsonFieldType.STRING).description("우편번호입니다."),
    };

    //USER_DESC
    protected FieldDescriptor[] signupRequestFields = new FieldDescriptor[] {
            fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일입니다. 이메일 형식이어야 합니다."),
            fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호입니다. 영문 숫자 조합 최소 8자 이상에서 최대 64자 이하여야 합니다."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름입니다. 최소 2자 이상에서 최대 12자 이하여야 합니다."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("사용자 프로필 이미지입니다.").optional(),
            fieldWithPath("provider").type(JsonFieldType.STRING).description("사용자 정보 제공자입니다.").optional(),
    };

    protected ParameterDescriptor[] usersParams = new ParameterDescriptor[] {
            parameterWithName("username").description("조회할 사용자 이름입니다.").optional(),
    };


    protected FieldDescriptor[] socialRequestFields = new FieldDescriptor[] {
            fieldWithPath("accessToken").type(JsonFieldType.STRING).description("SNS로부터 받은 액세스 토큰입니다."),
    };

    protected FieldDescriptor[] userUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("username").description("변경할 사용자 이름입니다. 최소 2자 이상에서 최대 12자 이하여야 합니다.").optional(),
            fieldWithPath("profileImageUrl").description("변경할 사용자 프로필 사진입니다. 주소 형식여야 합니다.").optional(),
            fieldWithPath("backgroundImageUrl").description("변경할 사용자 스튜디오 배경 사진입니다. 주소 형식여야 합니다.").optional(),
            fieldWithPath("introduction").description("변경할 사용자 소개입니다. 최대 200자 이하여야 합니다.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("변경할 사용자 주소입니다.").optional(),
    };

    protected FieldDescriptor[] loginRequestFields = new FieldDescriptor[] {
            fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일입니다."),
            fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호입니다."),
            fieldWithPath("provider").type(JsonFieldType.STRING).description("사용자 정보 제공자입니다.").optional(),
    };

    protected FieldDescriptor[] tokenRequestFields = new FieldDescriptor[] {
            fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰입니다."),
            fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰입니다."),
    };


    protected FieldDescriptor[] tokenFields = new FieldDescriptor[] {
            fieldWithPath("grantType").type(JsonFieldType.STRING).description("승인 타입입니다."),
            fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰입니다. api 요청 시 사용됩니다."),
            fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰입니다. 토큰 재발급에 사용됩니다."),
            fieldWithPath("accessTokenExpireDate").type(JsonFieldType.NUMBER).description("jwt 입니다. api 요청 시 사용됩니다."),
    };

    protected FieldDescriptor[] usersFields = new FieldDescriptor[] {
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional()
    };

    protected HeaderDescriptor[] jwtHeader = new HeaderDescriptor[] {
            headerWithName("X-AUTH-TOKEN").description("로그인 성공 시 발급받은 액세스토큰입니다.")
    };

    protected FieldDescriptor[] studioUserFields = new FieldDescriptor[] {
            fieldWithPath("id").description("스튜디오 사용자 아이디입니다."),
            fieldWithPath("username").description("스튜디오 사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("스튜디오 사용자 프로필 사진입니다.").optional(),
            fieldWithPath("backgroundImageUrl").description("스튜디오 사용자 배경 사진입니다.").optional(),
            fieldWithPath("introduction").description("스튜디오 사용자 소개글입니다.").optional(),
            fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("스튜디오 사용자 팔로워 수입니다.").optional(),
            fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("스튜디오 사용자 팔로잉 수입니다.").optional(),
            fieldWithPath("followed").type(JsonFieldType.BOOLEAN).description("스튜디오 사용자 팔로우 여부입니다.").optional(),
    };

    protected FieldDescriptor[] myStudioUserFields = new FieldDescriptor[] {
            fieldWithPath("id").description("마이스튜디오 사용자 아이디입니다."),
            fieldWithPath("username").description("마이스튜디오 사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("마이스튜디오 사용자 프로필 사진입니다.").optional(),
            fieldWithPath("backgroundImageUrl").description("마이스튜디오 사용자 배경 사진입니다.").optional(),
            fieldWithPath("introduction").description("마이스튜디오 사용자 소개글입니다.").optional(),
            fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("마이스튜디오 사용자 팔로워 수입니다.").optional(),
            fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("마이스튜디오 사용자 팔로잉 수입니다.").optional(),
    };


    protected FieldDescriptor[] albumUserFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 아이디입니다."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("introduction").type(JsonFieldType.STRING).description("사용자 소개글입니다.").optional(),
            fieldWithPath("followed").type(JsonFieldType.BOOLEAN).description("팔로우 여부입니다.").optional(),
    };


    protected FieldDescriptor[] userDetailFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 아이디입니다."),
            fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일입니다."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("introduction").type(JsonFieldType.STRING).description("사용자 소개글입니다.").optional(),
            fieldWithPath("provider").type(JsonFieldType.STRING).description("사용자 정보 제공자입니다.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("사용자 주소입니다.").optional(),
    };

    //ALBUM_DESC
    protected ParameterDescriptor[] albumsParams = new ParameterDescriptor[] {
            parameterWithName("title").description("조회할 앨범의 제목입니다.").optional(),
            parameterWithName("dateTimeFrom").description("조회할 앨범의 최소 시각입니다. 포맷은 yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS 입니다.").optional(),
            parameterWithName("dateTimeTo").description("조회할 앨범의 최대 시각입니다. 포맷은 yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS 입니다.").optional(),
    };

    protected ParameterDescriptor[] followingAlbumsParams = new ParameterDescriptor[] {
            parameterWithName("followingId").description("조회할 팔로잉의 아이디입니다.").optional(),
    };


    protected ParameterDescriptor[] userAlbumsParams = new ParameterDescriptor[] {
            parameterWithName("placeCond.region1DepthName").description("시도명입니다.").optional(),
            parameterWithName("placeCond.region2DepthName").description("시군구명입니다.").optional(),
            parameterWithName("placeCond.region3DepthName").description("읍면동명입니다.").optional(),
    };

    protected FieldDescriptor[] albumCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("title").description("앨범 제목입니다."),
            fieldWithPath("description").description("앨범 설명입니다.").optional(),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("앨범 사진 리스트입니다."),
    };

    protected FieldDescriptor[] albumUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("title").description("앨범 제목입니다."),
            fieldWithPath("description").description("앨범 설명입니다.").optional(),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("앨범 사진 리스트입니다."),
    };

    protected FieldDescriptor[] albumFields = new FieldDescriptor[] {
            fieldWithPath("id").description("앨범 아이디입니다."),
            fieldWithPath("createdAt").description("앨범 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").description("앨범 수정 시각입니다."),
            fieldWithPath("title").description("앨범 제목입니다."),
            fieldWithPath("description").description("앨범 설명입니다.").optional(),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("앨범 조회수입니다."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("앨범 등록자입니다."),
            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("앨범 좋아요수입니다."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("앨범 댓글수입니다."),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("앨범 좋아요 여부입니다."),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("앨범 사진 리스트입니다."),
    };

    protected FieldDescriptor[] albumsFields = new FieldDescriptor[] {
            fieldWithPath("id").description("앨범 아이디입니다."),
            fieldWithPath("createdAt").description("앨범 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").description("앨범 수정 시각입니다."),
            fieldWithPath("title").description("앨범 제목입니다."),
            fieldWithPath("description").description("앨범 설명입니다.").optional(),
            fieldWithPath("thumbnailPicture").type(JsonFieldType.OBJECT).description("앨범 썸네일 사진입니다."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("앨범 등록자입니다."),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("앨범 조회수입니다."),
            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("앨범 좋아요수입니다."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("앨범 댓글수입니다."),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("앨범 좋아요 여부입니다."),
    };

    protected FieldDescriptor[] studioAlbumsFields = new FieldDescriptor[] {
            fieldWithPath("id").description("앨범 아이디입니다."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("앨범 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("앨범 수정 시각입니다."),
            fieldWithPath("title").type(JsonFieldType.STRING).description("앨범 제목입니다."),
            fieldWithPath("thumbnailPicture").type(JsonFieldType.OBJECT).description("앨범 썸네일 사진입니다."),
    };

    //PICTURE_DESC
    protected FieldDescriptor[] pictureFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("사진 아이디입니다."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사진 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("사진 수정 시각입니다."),
            fieldWithPath("thumbnail").type(JsonFieldType.BOOLEAN).description("앨법 썸네일 여부입니다."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("사진 이미지 주소입니다."),
            fieldWithPath("description").type(JsonFieldType.STRING).description("사진 설명입니다.").optional(),
            fieldWithPath("place").type(JsonFieldType.OBJECT).description("사진 장소입니다."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("사진 댓글수입니다."),
    };

    protected FieldDescriptor[] thumbnailPictureFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("사진 아이디입니다."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사진 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("사진 수정 시각입니다."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("사진 이미지 주소입니다."),
    };


    protected FieldDescriptor[] pictureCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("thumbnail").type(JsonFieldType.BOOLEAN).description("썸네일 플래그입니다."),
            fieldWithPath("order").type(JsonFieldType.NUMBER).description("사진 순서입니다."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("사진 이미지 주소입니다."),
            fieldWithPath("description").type(JsonFieldType.STRING).description("사진 설명입니다.").optional(),
            fieldWithPath("place").type(JsonFieldType.OBJECT).description("사진 장소입니다."),
    };

    protected FieldDescriptor[] pictureUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("사진 이미지 주소입니다."),
            fieldWithPath("order").type(JsonFieldType.NUMBER).description("사진 순서입니다."),
            fieldWithPath("thumbnail").type(JsonFieldType.BOOLEAN).description("썸네일 플래그입니다."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("사진 이미지 주소입니다."),
            fieldWithPath("description").type(JsonFieldType.STRING).description("사진 설명입니다.").optional(),
            fieldWithPath("place").type(JsonFieldType.OBJECT).description("사진 장소입니다."),
    };

    //PLACE_DESC
    protected FieldDescriptor[] placeFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("장소 아이디입니다."),
            fieldWithPath("name").type(JsonFieldType.STRING).description("장소 이름입니다."),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("장소 위도입니다."),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("장소 경도입니다."),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("장소 주소입니다."),
    };

    protected FieldDescriptor[] placeCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("장소 이름입니다."),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("장소 위도입니다."),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("장소 경도입니다."),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("장소 주소입니다."),
    };

    protected FieldDescriptor[] placeUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("장소 이름입니다."),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("장소 위도입니다."),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("장소 경도입니다."),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("장소 주소입니다."),
    };

    //COMMENT_DESC
    protected FieldDescriptor[] commentCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용입니다."),
    };

    protected ParameterDescriptor[] commentCreatePathParams = new ParameterDescriptor[] {
            parameterWithName("pictureId").description("사진 아이디입니다."),
    };

    protected ParameterDescriptor[] commentDeletePathParams = new ParameterDescriptor[] {
            parameterWithName("pictureId").description("사진 아이디입니다."),
            parameterWithName("commentId").description("댓글 아이디입니다."),
    };

    protected FieldDescriptor[] commentsFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("댓글 아이디입니다."),
            fieldWithPath("pictureId").type(JsonFieldType.NUMBER).description("사진 아이디입니다."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("댓글 생성 시각입니다."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("댓글 수정 시각입니다."),
            fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용입니다."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("댓글 작성자입니다."),
    };

    //LIKE_DESC

    //FOLLOWER_DESC
    protected ParameterDescriptor[] followersParams = new ParameterDescriptor[] {
            parameterWithName("username").description("조회할 팔로워 이름입니다.").optional(),
    };

    protected ParameterDescriptor[] followingsParams = new ParameterDescriptor[] {
            parameterWithName("username").description("조회할 팔로잉 이름입니다.").optional(),
    };

    protected FieldDescriptor[] followingsSortByAlbumFields = new FieldDescriptor[] {
            fieldWithPath("id").description("사용자 아이디입니다."),
            fieldWithPath("username").description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("latestAlbumCreatedAt").description("사용자의 최신 앨범 생성 시각입니다.").optional(),
            fieldWithPath("latestAlbumLastModifiedAt").description("사용자의 최신 앨범 수정 시각입니다.").optional(),
    };

    protected FieldDescriptor[] followersFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 아이디입니다."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 이름입니다."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("사용자 프로필 사진입니다.").optional(),
            fieldWithPath("followed").type(JsonFieldType.BOOLEAN).description("사용자 팔로우 여부입니다.").optional(),
    };

}
