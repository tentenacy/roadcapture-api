package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.exception.AuthenticationEntryPointException;
import com.untilled.roadcapture.api.exception.CAccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
@RequiredArgsConstructor
public class ExceptionController {

    @GetMapping("/entrypoint")
    public void entryPointException() throws Exception {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/access-denied")
    public void accessDeniedException() {
        throw new CAccessDeniedException();
    }
}
