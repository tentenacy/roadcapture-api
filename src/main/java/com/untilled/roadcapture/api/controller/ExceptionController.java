package com.untilled.roadcapture.api.controller;

import com.untilled.roadcapture.api.exception.business.CEntityNotFoundException.CUserNotFoundException;
import com.untilled.roadcapture.api.exception.security.CTokenException.CAccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.untilled.roadcapture.api.exception.security.CSecurityException.*;

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
