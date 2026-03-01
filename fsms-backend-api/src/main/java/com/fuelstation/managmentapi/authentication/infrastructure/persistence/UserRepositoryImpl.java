package com.fuelstation.managmentapi.authentication.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import com.fuelstation.managmentapi.authentication.domain.User;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.authentication.domain.UserRole;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(jpaUserRepository.save(UserMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> findByIds(Iterable<Long> ids) {
        return jpaUserRepository.findAllById(ids).stream().map(UserMapper::toDomain).toList();
    }

    @Override
    public Optional<User> findByEmailAndRole(String email, UserRole role) {
        return jpaUserRepository.findByEmailAndUserRoleId(email, role.getId()).map(UserMapper::toDomain);
    }

}