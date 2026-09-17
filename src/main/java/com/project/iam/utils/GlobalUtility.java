package com.project.iam.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class GlobalUtility {
    public static String getCurrentCredUsername(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static List<String> getCurrentCredRole(){
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
