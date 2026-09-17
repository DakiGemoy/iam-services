package com.project.iam.models.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"code","message","exception","data"})
public class BasePaginationResponse<T>{
    private String code;
    private String message;
    private Integer currentPage;
    private Long totalPage;
    private Long totalData;
    private T data;
}
