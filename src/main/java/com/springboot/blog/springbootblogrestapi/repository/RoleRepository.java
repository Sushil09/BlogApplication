package com.springboot.blog.springbootblogrestapi.repository;

import com.springboot.blog.springbootblogrestapi.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String roleName);
}
