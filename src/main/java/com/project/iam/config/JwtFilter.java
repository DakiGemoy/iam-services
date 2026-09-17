package com.project.iam.config;

import com.project.iam.config.exception.ProcessException;
import com.project.iam.config.process.JwtUtil;
import com.project.iam.entities.UserEntity;
import com.project.iam.entities.UserRoleEntity;
import com.project.iam.repositories.UserRepository;
import com.project.iam.repositories.UserRoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.getClaimsFromToken(token);
                if (!jwtUtil.isTokenExpired(claims)) {
                    String username = claims.getSubject();
                    Date issuedAt = claims.getIssuedAt();

                    request.setAttribute("username", username);
                    request.setAttribute("issuedAt", formatDate(issuedAt));
                    List<String> rolesClaim = (List<String>) claims.get("roles");

                    List<SimpleGrantedAuthority> role = rolesClaim.stream().map(SimpleGrantedAuthority::new).toList();
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username,null, role);
                    auth.setDetails(token);

                    SecurityContextHolder.getContext().setAuthentication(auth);

                } else {
                    log.error("Token expired");
                    request.setAttribute("jwtMessage","Token expired");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (ExpiredJwtException e) {
                log.error("Token expired");
                request.setAttribute("jwtMessage","Token expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (SecurityException e) {
                log.error("Signature mismatch");
                request.setAttribute("jwtMessage","Invalid token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                log.error("Token invalid / Error breakdown token");
                request.setAttribute("jwtMessage","Invalid token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(date);
    }
}
