package com.project.iam.models.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token
) {
}
