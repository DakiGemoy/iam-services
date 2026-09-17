package com.project.iam.models.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AccessSubmissionResp(
        Long submissionId,
        String status,
        LocalDateTime lastUpdated,
        String lastUpdatedBy
) {
}
