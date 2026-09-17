package com.project.iam.controllers;

import com.project.iam.models.request.ApprovalSubmissionRequest;
import com.project.iam.models.response.BaseResponse;
import com.project.iam.services.AccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final AccessService accessService;

    @GetMapping
    public BaseResponse<?,?> getSubmissionNeedApprove(){
        return BaseResponse.builder()
                .code("00")
                .message("Success get approval data")
                .data(accessService.getListNeedApprove())
                .build();
    }

    @PatchMapping("/{id}/action")
    public BaseResponse<?,?> updateSubmission(@PathVariable("id") Integer requestAccesId,
                                              @Valid @RequestBody ApprovalSubmissionRequest request){
        accessService.updateSubmission(requestAccesId, request);
        return BaseResponse.builder()
                .code("00")
                .message("Success update request access")
                .build();
    }
}
