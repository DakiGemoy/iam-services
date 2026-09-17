package com.project.iam.models.response;

import lombok.Builder;

@Builder
public record AccessResp(
        String catalog,
        String baseUrl
) {
}
