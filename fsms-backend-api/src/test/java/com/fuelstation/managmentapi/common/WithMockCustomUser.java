package com.fuelstation.managmentapi.common;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "testuser";

    String email() default "test@example.com";

    UserRole role() default UserRole.ADMINISTRATOR;
}