package com.project.iam.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SubmissionRequest(
        @NotEmpty
        List<Items> items
) {
    @Valid
    public record Items(
        @NotBlank
        String accessCatalogName,
        String reason,
        String action
    ){}
}
