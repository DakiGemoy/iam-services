package com.project.iam.controllers;

import com.project.iam.models.response.BaseResponse;
import com.project.iam.services.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accesses")
@RequiredArgsConstructor
public class AccessController {
    private final AccessService accessService;

    @GetMapping("/get")
    public BaseResponse<?,?> getListAccess(){
        return BaseResponse.builder()
                .code("00")
                .message("Success")
                .data(accessService.getAllAccessData())
                .build();
    }
}
