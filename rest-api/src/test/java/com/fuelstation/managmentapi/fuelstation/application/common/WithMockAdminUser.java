package com.fuelstation.managmentapi.fuelstation.application.common;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@WithMockUser(username = TestSettings.TEST_ADMIN_EMAIL, authorities = {"ADMINISTRATOR"})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WithMockAdminUser {}