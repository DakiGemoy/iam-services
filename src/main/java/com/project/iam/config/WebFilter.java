package com.project.iam.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class WebFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, (1024 * 50));

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String trxId = UUID.randomUUID().toString().replace("-","");
        ThreadContext.put("TRX_ID",trxId);
        response.setHeader("TRX_ID",trxId);
        long startTime = System.currentTimeMillis();

        log.info("Incoming request [{}] : Origin [{}], Target [{} {}], Params [{}]",
                trxId, request.getHeader("Origin"),
                request.getMethod(), request.getRequestURI(),
                flattenParamMap(request.getParameterMap()));

        filterChain.doFilter(requestWrapper, responseWrapper);

        logRequestBody(requestWrapper);
        logResponseBody(responseWrapper);

        log.info("Request [{}] completed in {} ms", trxId, (System.currentTimeMillis() - startTime));

        responseWrapper.copyBodyToResponse();
        ThreadContext.clearAll();
    }

    protected void maskNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();

                if (fieldName.equalsIgnoreCase("password")) {
                    objectNode.put(fieldName, "*************");
                } else {
                    maskNode(entry.getValue());
                }
            }
        } else if (node.isArray()) {
            for (JsonNode child : node) {
                maskNode(child);
            }
        }
    }

    private String maskingRequestJson(String jsonBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(jsonBody);
            maskNode(node);
            return mapper.writeValueAsString(node);
        } catch (Exception e){
            return jsonBody;
        }
    }

    private String flattenParamMap(Map<String,String[]> params){
        StringBuilder build= new StringBuilder();
        if (params.isEmpty())
            return "";
        else{
            int counter=0;
            for (var m : params.entrySet()){
                build.append(m.getKey()).append("->");
                build.append(m.getValue()[0]);

                if (counter<(params.size()-1)){
                    build.append(", ");
                }

                counter++;
            }
        }
        return build.toString();
    }
    private void logRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length > 0) {
            String body = new String(buf, StandardCharsets.UTF_8);
            body = maskingRequestJson(body);
            log.info("[Request Body] : {}", body.isBlank() ? "-" : body);
        } else {
            log.info("[Request Body] : -");
        }
    }

    private void logResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length > 0) {
            String contentType = response.getContentType();

            if (contentType != null && contentType.contains("application/octet-stream")) {
                log.info("[Response] : -");
                return;
            }

            String body = new String(buf, StandardCharsets.UTF_8);
            log.info("[Response] : {}", body.isBlank() ? "-" : body);
        }
    }
}
