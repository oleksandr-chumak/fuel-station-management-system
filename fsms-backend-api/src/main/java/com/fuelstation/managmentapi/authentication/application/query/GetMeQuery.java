package com.fuelstation.managmentapi.authentication.application.query;


import com.fuelstation.managmentapi.authentication.application.UserNotFoundException;
import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetMeQuery {

    private final UserRepository userRepository;

    @Transactional
    public User process(String username) {
        long userId = Long.parseLong(username);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
