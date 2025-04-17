package com.fuelstation.managmentapi.administrator.infrastructure.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fuelstation.managmentapi.administrator.domain.Administrator;
import com.fuelstation.managmentapi.administrator.domain.AdministratorRepository;
import com.fuelstation.managmentapi.authentication.infrastructure.persistence.CredentialsEntity;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class AdministratorRepositoryImpl implements AdministratorRepository {

    @Autowired
    private JpaAdministratorRepository jpaAdministratorRepository;

    @Autowired
    private EntityManager em;

    @Override
    public Administrator save(Administrator administrator) {
        AdministratorEntity entity = AdministratorMapper.toEntity(administrator);
        entity = jpaAdministratorRepository.save(entity);
        return AdministratorMapper.toDomain(entity);
    }

    @Override
    public Optional<Administrator> findById(Long id) {
        return jpaAdministratorRepository.findById(id).map(entity -> AdministratorMapper.toDomain(entity));
    }

    @Override
    public Optional<Administrator> findByEmail(String email) {
        return jpaAdministratorRepository.findByCredentialsEmail(email).map(AdministratorMapper::toDomain);
    }

    @Override
    @Transactional
    public Administrator create(Long credentialsId) {
        CredentialsEntity credentialsEntity = em.getReference(CredentialsEntity.class, credentialsId);
        
        AdministratorEntity administratorEntity = new AdministratorEntity(null, credentialsEntity);
        administratorEntity = jpaAdministratorRepository.save(administratorEntity);
        
        return AdministratorMapper.toDomain(administratorEntity);
    }   
}
    