package com.project.iam.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.iam.models.response.BaseResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message = request.getAttribute("jwtMessage")!=null ? (String) request.getAttribute("jwtMessage") : "Token required";

        BaseResponse<?,?> resp = BaseResponse.builder()
                .code("401")
                .message(message)
                .build();

        response.getWriter().write((new ObjectMapper()).writeValueAsString(resp));
        response.getWriter().flush();
    }
}
