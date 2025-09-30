package com.system.bank_manager.repository;

import org.springframework.stereotype.Repository;
import com.system.bank_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDni(String dni);
}



