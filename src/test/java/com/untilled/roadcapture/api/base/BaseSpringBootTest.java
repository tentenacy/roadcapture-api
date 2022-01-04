package com.untilled.roadcapture.api.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.untilled.roadcapture.api.service.UserService;
import com.untilled.roadcapture.domain.token.RefreshTokenRepository;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles({"test"})
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseSpringBootTest {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected Environment env;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected MockMvc mockMvc;

    @SpyBean
    protected UserService userService;

}
