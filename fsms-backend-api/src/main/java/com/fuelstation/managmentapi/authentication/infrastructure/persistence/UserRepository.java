package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.authentication.domain.User;
import com.fuelstation.managmentapi.authentication.domain.UserRole;

public interface UserRepository {
     User save(User user);
     Optional<User> findById(Long id);
     List<User> findByIds(Iterable<Long> ids);
     Optional<User> findByEmailAndRole(String email, UserRole role);
}
