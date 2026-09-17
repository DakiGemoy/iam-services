package com.project.iam.controllers;

import com.project.iam.models.response.BaseResponse;
import com.project.iam.services.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AccessService accessService;

    @GetMapping("/metrics")
    public BaseResponse<?,?> getDashboardData(){
        return BaseResponse.builder()
                .code("00")
                .message("Success get dashboard data")
                .data(accessService.getDashboardData())
                .build();
    }
}
