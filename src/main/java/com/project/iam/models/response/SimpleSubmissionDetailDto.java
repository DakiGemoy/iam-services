package com.project.iam.models.response;

import lombok.Builder;

@Builder
public record SimpleSubmissionDetailDto(
        String catalogName,
        String action
) {
}
