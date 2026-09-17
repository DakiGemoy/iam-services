package com.project.iam.models.request;

import jakarta.validation.constraints.NotBlank;

public record ApprovalSubmissionRequest(
        @NotBlank
        String action,
        String notes
) {
}
