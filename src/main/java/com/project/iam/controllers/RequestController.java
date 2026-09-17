package com.project.iam.controllers;

import com.project.iam.models.request.SubmissionRequest;
import com.project.iam.models.response.BaseResponse;
import com.project.iam.services.AccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController {

    private final AccessService accessService;

    @PostMapping
    public BaseResponse<?,?> submitNewRequest(@Valid @RequestBody SubmissionRequest request){
        accessService.postSubmissionAccess(request);
        return BaseResponse.builder()
                .code("00")
                .message("Success create new request")
                .build();
    }
}
