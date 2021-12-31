package com.untilled.roadcapture.config.security;

import com.untilled.roadcapture.api.dto.common.ErrorCode;
import com.untilled.roadcapture.api.exception.security.CAuthenticationEntryPointException;
import com.untilled.roadcapture.api.exception.business.CUserNotFoundException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        log.info("[Verifying token]");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        try {
            if(StringUtils.hasText(token) && jwtProvider.validationToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new CAuthenticationEntryPointException();
            }
        } catch (CUserNotFoundException e) {
            request.setAttribute("exception", ErrorCode.USER_NOT_FOUND.getCode());
        } catch (CAuthenticationEntryPointException e) {
            request.setAttribute("exception", ErrorCode.ACCESS_TOKEN_ERROR.getCode());
        } finally {
            chain.doFilter(request, response);
        }
    }
}
