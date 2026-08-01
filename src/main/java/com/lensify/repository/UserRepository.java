package com.lensify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lensify.entity.Role;
import com.lensify.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("""
			SELECT u
			FROM User u
			JOIN FETCH u.role
			WHERE u.username = :username
			""")
			Optional<User> findByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByRole(Role role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}