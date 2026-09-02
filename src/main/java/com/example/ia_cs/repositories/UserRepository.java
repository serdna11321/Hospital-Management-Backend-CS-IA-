package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Person;
import com.example.ia_cs.entities.Role;
import com.example.ia_cs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    String getPasswordHash(String email);
    Optional<User> findByPerson(Person person);
    boolean existsByPerson(Person person);
    List<User> getAll();
    List<Role> getRolesFromEmail(String email);
    long getUserIdFromEmail(String email);
}
