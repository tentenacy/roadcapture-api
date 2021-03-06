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
            parameterWithName("page").description("????????? ??????????????????. 0?????? ???????????????.").optional(),
            parameterWithName("size").description("??? ???????????? ????????? ????????? ????????????.").optional(),
            parameterWithName("sort").description("?????? ???????????????.").optional()
    };

    protected FieldDescriptor[] pageFields = new FieldDescriptor[] {
            fieldWithPath("content").type(JsonFieldType.ARRAY).description("????????? ?????? ??????????????????."),
            fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("????????? ????????? ???????????????."),
            fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("?????? ?????? ????????????."),
            fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("?????? ????????? ????????????."),
            fieldWithPath("size").type(JsonFieldType.NUMBER).description("??? ???????????? ????????? ????????? ????????????."),
            fieldWithPath("number").type(JsonFieldType.NUMBER).ignored(),
            fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("??? ????????? ???????????????."),
            fieldWithPath("numberOfElements").ignored(),
            fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("???????????? ?????? ????????? ???????????????."),
            subsectionWithPath("sort").type(JsonFieldType.OBJECT).ignored(),
            subsectionWithPath("pageable").type(JsonFieldType.OBJECT).ignored(),
    };

    protected FieldDescriptor[] badFields = new FieldDescriptor[] {
            fieldWithPath("code").description("?????? ???????????????."),
            fieldWithPath("status").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("message").description("?????? ??????????????????."),
            fieldWithPath("errors").description("?????? ???????????????. ?????? ?????? ????????? ???????????????.")
    };

    protected FieldDescriptor[] errorsFields = new FieldDescriptor[] {
            fieldWithPath("field").description("????????? ????????? ??????????????????."),
            fieldWithPath("value").description("????????? ????????? ??????????????????."),
            fieldWithPath("reason").description("????????? ????????? ???????????????.")
    };

    //VALUE_DESC
    protected FieldDescriptor[] addressFields = new FieldDescriptor[] {
            fieldWithPath("addressName").type(JsonFieldType.STRING).description("?????????????????????."),
            fieldWithPath("roadAddressName").type(JsonFieldType.STRING).description("????????????????????????.").optional(),
            fieldWithPath("region1DepthName").type(JsonFieldType.STRING).description("??????????????????."),
            fieldWithPath("region2DepthName").type(JsonFieldType.STRING).description("?????????????????????."),
            fieldWithPath("region3DepthName").type(JsonFieldType.STRING).description("?????????????????????."),
            fieldWithPath("zoneNo").type(JsonFieldType.STRING).description("?????????????????????."),
    };

    //USER_DESC
    protected FieldDescriptor[] signupRequestFields = new FieldDescriptor[] {
            fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ??????????????????. ????????? ??????????????? ?????????."),
            fieldWithPath("password").type(JsonFieldType.STRING).description("????????? ?????????????????????. ?????? ?????? ?????? ?????? 8??? ???????????? ?????? 64??? ???????????? ?????????."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("????????? ???????????????. ?????? 2??? ???????????? ?????? 12??? ???????????? ?????????."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("????????? ????????? ??????????????????.").optional(),
            fieldWithPath("provider").type(JsonFieldType.STRING).description("????????? ?????? ??????????????????.").optional(),
    };

    protected ParameterDescriptor[] usersParams = new ParameterDescriptor[] {
            parameterWithName("username").description("????????? ????????? ???????????????.").optional(),
    };


    protected FieldDescriptor[] socialRequestFields = new FieldDescriptor[] {
            fieldWithPath("accessToken").type(JsonFieldType.STRING).description("SNS????????? ?????? ????????? ???????????????."),
    };

    protected FieldDescriptor[] userUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("username").description("????????? ????????? ???????????????. ?????? 2??? ???????????? ?????? 12??? ???????????? ?????????.").optional(),
            fieldWithPath("profileImageUrl").description("????????? ????????? ????????? ???????????????. ?????? ???????????? ?????????.").optional(),
            fieldWithPath("backgroundImageUrl").description("????????? ????????? ???????????? ?????? ???????????????. ?????? ???????????? ?????????.").optional(),
            fieldWithPath("introduction").description("????????? ????????? ???????????????. ?????? 200??? ???????????? ?????????.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("????????? ????????? ???????????????.").optional(),
    };

    protected FieldDescriptor[] loginRequestFields = new FieldDescriptor[] {
            fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ??????????????????."),
            fieldWithPath("password").type(JsonFieldType.STRING).description("????????? ?????????????????????."),
            fieldWithPath("provider").type(JsonFieldType.STRING).description("????????? ?????? ??????????????????.").optional(),
    };

    protected FieldDescriptor[] tokenRequestFields = new FieldDescriptor[] {
            fieldWithPath("accessToken").type(JsonFieldType.STRING).description("????????? ???????????????."),
            fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("???????????? ???????????????."),
    };


    protected FieldDescriptor[] tokenFields = new FieldDescriptor[] {
            fieldWithPath("grantType").type(JsonFieldType.STRING).description("?????? ???????????????."),
            fieldWithPath("accessToken").type(JsonFieldType.STRING).description("????????? ???????????????. api ?????? ??? ???????????????."),
            fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("???????????? ???????????????. ?????? ???????????? ???????????????."),
            fieldWithPath("accessTokenExpireDate").type(JsonFieldType.NUMBER).description("jwt ?????????. api ?????? ??? ???????????????."),
    };

    protected FieldDescriptor[] usersFields = new FieldDescriptor[] {
            fieldWithPath("id").description("????????? ??????????????????."),
            fieldWithPath("username").description("????????? ???????????????."),
            fieldWithPath("profileImageUrl").description("????????? ????????? ???????????????.").optional()
    };

    protected HeaderDescriptor[] jwtHeader = new HeaderDescriptor[] {
            headerWithName("X-AUTH-TOKEN").description("????????? ?????? ??? ???????????? ????????????????????????.")
    };

    protected FieldDescriptor[] studioUserFields = new FieldDescriptor[] {
            fieldWithPath("id").description("???????????? ????????? ??????????????????."),
            fieldWithPath("username").description("???????????? ????????? ???????????????."),
            fieldWithPath("profileImageUrl").description("???????????? ????????? ????????? ???????????????.").optional(),
            fieldWithPath("backgroundImageUrl").description("???????????? ????????? ?????? ???????????????.").optional(),
            fieldWithPath("introduction").description("???????????? ????????? ??????????????????.").optional(),
            fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("???????????? ????????? ????????? ????????????.").optional(),
            fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("???????????? ????????? ????????? ????????????.").optional(),
            fieldWithPath("followed").type(JsonFieldType.BOOLEAN).description("???????????? ????????? ????????? ???????????????.").optional(),
    };

    protected FieldDescriptor[] myStudioUserFields = new FieldDescriptor[] {
            fieldWithPath("id").description("?????????????????? ????????? ??????????????????."),
            fieldWithPath("username").description("?????????????????? ????????? ???????????????."),
            fieldWithPath("profileImageUrl").description("?????????????????? ????????? ????????? ???????????????.").optional(),
            fieldWithPath("backgroundImageUrl").description("?????????????????? ????????? ?????? ???????????????.").optional(),
            fieldWithPath("introduction").description("?????????????????? ????????? ??????????????????.").optional(),
            fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("?????????????????? ????????? ????????? ????????????.").optional(),
            fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("?????????????????? ????????? ????????? ????????????.").optional(),
    };


    protected FieldDescriptor[] albumUserFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ??????????????????."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("????????? ???????????????."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("????????? ????????? ???????????????.").optional(),
            fieldWithPath("introduction").type(JsonFieldType.STRING).description("????????? ??????????????????.").optional(),
            fieldWithPath("followed").type(JsonFieldType.BOOLEAN).description("????????? ???????????????.").optional(),
    };


    protected FieldDescriptor[] userDetailFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ??????????????????."),
            fieldWithPath("email").type(JsonFieldType.STRING).description("????????? ??????????????????."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("????????? ???????????????."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("????????? ????????? ???????????????.").optional(),
            fieldWithPath("introduction").type(JsonFieldType.STRING).description("????????? ??????????????????.").optional(),
            fieldWithPath("provider").type(JsonFieldType.STRING).description("????????? ?????? ??????????????????.").optional(),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("????????? ???????????????.").optional(),
    };

    //ALBUM_DESC
    protected ParameterDescriptor[] albumsParams = new ParameterDescriptor[] {
            parameterWithName("title").description("????????? ????????? ???????????????.").optional(),
            parameterWithName("dateTimeFrom").description("????????? ????????? ?????? ???????????????. ????????? yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS ?????????.").optional(),
            parameterWithName("dateTimeTo").description("????????? ????????? ?????? ???????????????. ????????? yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS ?????????.").optional(),
    };

    protected ParameterDescriptor[] followingAlbumsParams = new ParameterDescriptor[] {
            parameterWithName("followingId").description("????????? ???????????? ??????????????????.").optional(),
    };


    protected ParameterDescriptor[] userAlbumsParams = new ParameterDescriptor[] {
            parameterWithName("placeCond.region1DepthName").description("??????????????????.").optional(),
            parameterWithName("placeCond.region2DepthName").description("?????????????????????.").optional(),
            parameterWithName("placeCond.region3DepthName").description("?????????????????????.").optional(),
    };

    protected FieldDescriptor[] albumCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("title").description("?????? ???????????????."),
            fieldWithPath("description").description("?????? ???????????????.").optional(),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("?????? ?????? ??????????????????."),
    };

    protected FieldDescriptor[] albumUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("title").description("?????? ???????????????."),
            fieldWithPath("description").description("?????? ???????????????.").optional(),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("?????? ?????? ??????????????????."),
    };

    protected FieldDescriptor[] albumFields = new FieldDescriptor[] {
            fieldWithPath("id").description("?????? ??????????????????."),
            fieldWithPath("createdAt").description("?????? ?????? ???????????????."),
            fieldWithPath("lastModifiedAt").description("?????? ?????? ???????????????."),
            fieldWithPath("title").description("?????? ???????????????."),
            fieldWithPath("description").description("?????? ???????????????.").optional(),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("?????? ??????????????????."),
            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("?????? ?????????????????????."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ????????? ???????????????."),
            fieldWithPath("pictures").type(JsonFieldType.ARRAY).description("?????? ?????? ??????????????????."),
    };

    protected FieldDescriptor[] albumsFields = new FieldDescriptor[] {
            fieldWithPath("id").description("?????? ??????????????????."),
            fieldWithPath("createdAt").description("?????? ?????? ???????????????."),
            fieldWithPath("lastModifiedAt").description("?????? ?????? ???????????????."),
            fieldWithPath("title").description("?????? ???????????????."),
            fieldWithPath("description").description("?????? ???????????????.").optional(),
            fieldWithPath("thumbnailPicture").type(JsonFieldType.OBJECT).description("?????? ????????? ???????????????."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("?????? ??????????????????."),
            fieldWithPath("viewCount").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("?????? ?????????????????????."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("?????? ????????? ???????????????."),
    };

    protected FieldDescriptor[] studioAlbumsFields = new FieldDescriptor[] {
            fieldWithPath("id").description("?????? ??????????????????."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("title").type(JsonFieldType.STRING).description("?????? ???????????????."),
            fieldWithPath("thumbnailPicture").type(JsonFieldType.OBJECT).description("?????? ????????? ???????????????."),
    };

    //PICTURE_DESC
    protected FieldDescriptor[] pictureFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("thumbnail").type(JsonFieldType.BOOLEAN).description("?????? ????????? ???????????????."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ???????????????."),
            fieldWithPath("description").type(JsonFieldType.STRING).description("?????? ???????????????.").optional(),
            fieldWithPath("place").type(JsonFieldType.OBJECT).description("?????? ???????????????."),
            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
    };

    protected FieldDescriptor[] thumbnailPictureFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ???????????????."),
    };


    protected FieldDescriptor[] pictureCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("thumbnail").type(JsonFieldType.BOOLEAN).description("????????? ??????????????????."),
            fieldWithPath("order").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ???????????????."),
            fieldWithPath("description").type(JsonFieldType.STRING).description("?????? ???????????????.").optional(),
            fieldWithPath("place").type(JsonFieldType.OBJECT).description("?????? ???????????????."),
    };

    protected FieldDescriptor[] pictureUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ????????? ???????????????."),
            fieldWithPath("order").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("thumbnail").type(JsonFieldType.BOOLEAN).description("????????? ??????????????????."),
            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("?????? ????????? ???????????????."),
            fieldWithPath("description").type(JsonFieldType.STRING).description("?????? ???????????????.").optional(),
            fieldWithPath("place").type(JsonFieldType.OBJECT).description("?????? ???????????????."),
    };

    //PLACE_DESC
    protected FieldDescriptor[] placeFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ???????????????."),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("?????? ???????????????."),
    };

    protected FieldDescriptor[] placeCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ???????????????."),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("?????? ???????????????."),
    };

    protected FieldDescriptor[] placeUpdateRequestFields = new FieldDescriptor[] {
            fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ???????????????."),
            fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("?????? ???????????????."),
            fieldWithPath("address").type(JsonFieldType.OBJECT).description("?????? ???????????????."),
    };

    //COMMENT_DESC
    protected FieldDescriptor[] commentCreateRequestFields = new FieldDescriptor[] {
            fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ???????????????."),
    };

    protected ParameterDescriptor[] commentCreatePathParams = new ParameterDescriptor[] {
            parameterWithName("pictureId").description("?????? ??????????????????."),
    };

    protected ParameterDescriptor[] commentDeletePathParams = new ParameterDescriptor[] {
            parameterWithName("pictureId").description("?????? ??????????????????."),
            parameterWithName("commentId").description("?????? ??????????????????."),
    };

    protected FieldDescriptor[] commentsFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("pictureId").type(JsonFieldType.NUMBER).description("?????? ??????????????????."),
            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("lastModifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ???????????????."),
            fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ???????????????."),
            fieldWithPath("user").type(JsonFieldType.OBJECT).description("?????? ??????????????????."),
    };

    //LIKE_DESC

    //FOLLOWER_DESC
    protected ParameterDescriptor[] followersParams = new ParameterDescriptor[] {
            parameterWithName("username").description("????????? ????????? ???????????????.").optional(),
    };

    protected ParameterDescriptor[] followingsParams = new ParameterDescriptor[] {
            parameterWithName("username").description("????????? ????????? ???????????????.").optional(),
    };

    protected FieldDescriptor[] followingsSortByAlbumFields = new FieldDescriptor[] {
            fieldWithPath("id").description("????????? ??????????????????."),
            fieldWithPath("username").description("????????? ???????????????."),
            fieldWithPath("profileImageUrl").description("????????? ????????? ???????????????.").optional(),
            fieldWithPath("latestAlbumCreatedAt").description("???????????? ?????? ?????? ?????? ???????????????.").optional(),
            fieldWithPath("latestAlbumLastModifiedAt").description("???????????? ?????? ?????? ?????? ???????????????.").optional(),
    };

    protected FieldDescriptor[] followersFields = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ??????????????????."),
            fieldWithPath("username").type(JsonFieldType.STRING).description("????????? ???????????????."),
            fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("????????? ????????? ???????????????.").optional(),
            fieldWithPath("followed").type(JsonFieldType.BOOLEAN).description("????????? ????????? ???????????????.").optional(),
    };

}
