package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByNif(String nif);
}
