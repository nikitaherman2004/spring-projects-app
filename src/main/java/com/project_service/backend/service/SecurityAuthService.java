package com.project_service.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SecurityAuthService {

    @Value("${security.attributes.role}")
    private String roleHeaderName;

    @Value("${security.attributes.sub}")
    private String subHeaderName;

    private final PasswordEncoder passwordEncoder;

    public boolean matchesPassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String getAuthenticationPrincipalName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        return (String) securityContext.getAuthentication().getPrincipal();
    }

    public Authentication createAuthenticationFromRequest(HttpServletRequest request) {
        String sub = getHeaderValueFromRequest(request, subHeaderName);
        String userRole = getHeaderValueFromRequest(request, roleHeaderName);

        Set<GrantedAuthority> grantedAuthorities = Set.of(new SimpleGrantedAuthority(userRole));

        return new UsernamePasswordAuthenticationToken(sub, null, grantedAuthorities);
    }

    private String getHeaderValueFromRequest(HttpServletRequest request, String headerName) {
        String headerValue = request.getHeader(headerName);

        if (headerValue == null) {
            throw new AccessDeniedException("Header is not present");
        }

        return headerValue;
    }
}
