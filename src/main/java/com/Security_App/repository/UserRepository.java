package com.Security_App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Security_App.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByPhoneNumber(String phoneNumber);

	Optional<User> findByEmail(String email);

	User findByEmailAndStatus(String email, String status);


	List<User> findByStatusIn(List<String> asList);

	User findByIdAndStatus(Long userId, String status);
}
