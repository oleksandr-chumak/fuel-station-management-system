package com.fuelstation.managmentapi.authentication.application;

import org.springframework.test.web.servlet.ResultActions;

public interface AuthTestClient {
   ResultActions loginManager(AuthRequest authRequest) throws Exception;
   String loginManagerAndGetToken(AuthRequest authRequest) throws Exception;

   ResultActions loginAdmin(AuthRequest authRequest) throws Exception;
   String loginAdminAndGetToken(AuthRequest authRequest) throws Exception;

   ResultActions getMe(String token) throws Exception;
   Me getMeAndReturnResponse(String token) throws Exception;
}
