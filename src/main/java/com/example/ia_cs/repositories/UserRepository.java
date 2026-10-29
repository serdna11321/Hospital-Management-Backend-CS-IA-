package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Person;
import com.example.ia_cs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userEmail = :userEmail")
    Optional<User> findByUserEmail(@Param("userEmail") String email);
    Optional<User> findByPerson(Person person);
    List<User> findAll();
    @Query("SELECT u.userID FROM User u WHERE u.userEmail = :userEmail")
    long findUserIdFromUserEmail(@Param("userEmail") String userEmail);
    Optional<User> findByUserID(long Id);

}
