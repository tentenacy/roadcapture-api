package com.untilled.roadcapture.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Rest Api이므로 비 인증 시 로그인 폼 화면으로 리다이렉트 하지 않음
                .httpBasic().disable()
                //Rest Api이므로 상태를 저장하지 않으므로 csrf 보안 설정하지 않음
                .csrf().disable()
                //Jwt 인증 시 세션이 필요하지 않으므로 생성하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/users",
                        "/users/token",
                        "/users/token/reissue",
                        "/users/social/kakao/**",
                        "/users/social/google/**",
                        "/users/social/naver/**"
                ).permitAll()
                .antMatchers(HttpMethod.GET, "/oauth/kakao/**").permitAll()
                .antMatchers(HttpMethod.GET, "/oauth/google/**").permitAll()
                .antMatchers(HttpMethod.GET, "/oauth/naver/**").permitAll()
                .antMatchers(HttpMethod.GET, "/exception/**").permitAll()
                .antMatchers(HttpMethod.POST, "/exception/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/exception/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/exception/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/exception/**").permitAll()
                .antMatchers(HttpMethod.HEAD, "/exception/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .anyRequest().hasRole("USER")
        .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        .and()
                //Jwt 인증 필터를 UsernamePasswordAuthenticationFilter 전에 삽입
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/docs/**");
    }
}
