package com.project_service.backend.util;

import com.project_service.backend.exception.ApplicationException;
import com.project_service.backend.service.SecurityAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnershipRequestValidator {

    private final SecurityAuthService securityAuthService;

    public void validateRequestOwnership(String fromRequestSub) {
        String sub = securityAuthService.getAuthenticationPrincipalName();

        if (!fromRequestSub.equals(sub)) {
            throw new ApplicationException("Некорректно заданы параметры запроса", HttpStatus.BAD_REQUEST);
        }
    }
}
