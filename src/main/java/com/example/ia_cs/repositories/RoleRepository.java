package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Role;
import com.example.ia_cs.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleName roleName);
}
