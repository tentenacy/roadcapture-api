package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.exception.security.CAuthenticationEntryPointException;
import com.untilled.roadcapture.api.exception.security.CAccessDeniedException;
import com.untilled.roadcapture.api.exception.business.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exception")
@RequiredArgsConstructor
public class ExceptionController {

    @RequestMapping("/entrypoint")
    public void entryPointException() throws Exception {
        throw new CAuthenticationEntryPointException();
    }

    @RequestMapping("/access-denied")
    public void accessDeniedException() {
        throw new CAccessDeniedException();
    }

    @RequestMapping(value = "/user-not-found")
    public void userNotFoundException() {
        throw new CUserNotFoundException();
    }
}
