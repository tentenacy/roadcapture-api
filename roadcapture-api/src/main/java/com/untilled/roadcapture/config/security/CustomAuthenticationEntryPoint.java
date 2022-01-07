package com.untilled.roadcapture.config.security;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if(ObjectUtils.isEmpty(exception)) return;

        if (exception.equals(ErrorCode.USER_NOT_FOUND.getCode())) {
            response.sendRedirect("/exception/user-not-found");
        } else if (exception.equals(ErrorCode.ACCESS_TOKEN_ERROR.getCode())) {
            response.sendRedirect("/exception/entrypoint");
        }
    }
}
