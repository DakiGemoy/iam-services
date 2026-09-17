package com.project.iam.controllers;

import com.project.iam.models.request.LoginRequest;
import com.project.iam.models.response.BaseResponse;
import com.project.iam.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<?,?> login(@Valid @RequestBody LoginRequest request){
        return BaseResponse.builder()
                .code("00")
                .message("Success login")
                .data(authService.login(request))
                .build();
    }
}
