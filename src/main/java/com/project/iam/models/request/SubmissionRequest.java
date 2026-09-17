package com.project.iam.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SubmissionRequest(
        @Valid
        @NotEmpty
        List<Items> items,
        String reason
) {
//    @Valid
    public record Items(
        @NotBlank
        String accessCatalogName,
        @NotBlank
        String action
    ){}
}
