package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.Administrator;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Person> findByPersonPersonID(long personID);
}
