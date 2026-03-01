package com.fuelstation.managmentapi.common;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.UserStatus;
import com.fuelstation.managmentapi.authentication.infrastructure.security.SecurityUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        var user = new User(
                1L,
                null,
                null,
                null,
                annotation.email(),
                UserStatus.ACTIVE,
                annotation.role(),
                "password"
        );
        var principal = new SecurityUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                "password",
                principal.getAuthorities()
        );

        context.setAuthentication(auth);
        return context;
    }
}

