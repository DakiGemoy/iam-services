package com.project.iam.models.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"code","message","exception","data"})
public class BaseResponse<T,E>{
    private String code;
    private String message;
    private E exception;
    private T data;
}
