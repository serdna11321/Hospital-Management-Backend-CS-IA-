package com.example.ia_cs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.Administrator;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    boolean existsWithPersonID(long personID);
}
