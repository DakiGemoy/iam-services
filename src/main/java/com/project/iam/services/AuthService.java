package com.project.iam.services;

import com.project.iam.config.exception.ProcessException;
import com.project.iam.config.process.JwtUtil;
import com.project.iam.models.request.LoginRequest;
import com.project.iam.models.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request){
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            Map<String, Object> claims = new HashMap<>();
            auth.getAuthorities();
            claims.put("roles",auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

            return LoginResponse.builder()
                    .token(jwtUtil.generateToken(request.username(),claims))
                    .build();
        } catch (BadCredentialsException e) {
            log.error("Error bad credential",e);
            throw new ProcessException(HttpStatus.UNAUTHORIZED,"401","Invalid username or password");
        } catch (ResourceAccessException e) {
            log.error("Error resource access",e);
            throw new ProcessException(HttpStatus.UNAUTHORIZED,"04","Service unavaliable");
        } catch (Exception e){
            log.error("Error unhandle",e);
            throw new ProcessException(HttpStatus.UNAUTHORIZED,"Error validasi user");
        }
    }
}
