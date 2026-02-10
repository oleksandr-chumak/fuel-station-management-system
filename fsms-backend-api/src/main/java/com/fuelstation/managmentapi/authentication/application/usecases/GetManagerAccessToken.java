package com.fuelstation.managmentapi.authentication.application.usecases;

import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsRepository;
import com.fuelstation.managmentapi.authentication.infrastructure.security.JwtTokenService;
import com.fuelstation.managmentapi.authentication.infrastructure.security.SecurityUserDetails;
import com.fuelstation.managmentapi.manager.application.usecases.GetManagerByCredentialsId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetManagerAccessToken {

    private final GetManagerByCredentialsId getManagerByCredentialsId;
    private final CredentialsRepository credentialsRepository;
    private final JwtTokenService jwtTokenService;

    public String process(long managerId) {
        var manager = getManagerByCredentialsId.process(managerId);
        var credentials = credentialsRepository.findById(manager.getCredentialsId())
                .orElseThrow(() -> new IllegalStateException("Credentials id: " + manager.getCredentialsId() + " does not exist"));

        return jwtTokenService.generateAccessToken(new SecurityUserDetails(credentials));
    }

}
