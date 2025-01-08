package com.project_service.backend.filter;

import com.project_service.backend.service.SecurityAuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAuthFilter extends OncePerRequestFilter {

    private final SecurityAuthService securityAuthService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain
    ) {
        try {
            Authentication authentication = securityAuthService.createAuthenticationFromRequest(request);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (AccessDeniedException exception) {
            log.error(
                    "An authorization error has occurred, and an error message has been sent {}",
                    exception.getMessage()
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Не удалось авторизоваться. Повторите попытку");
        }
    }
}
