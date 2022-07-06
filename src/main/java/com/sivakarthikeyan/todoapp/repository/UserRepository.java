package com.sivakarthikeyan.todoapp.repository;

import com.sivakarthikeyan.todoapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);

    Boolean existsByEmail(String email);

    Boolean existsByIdAndPassword(Long id, String password);
}
