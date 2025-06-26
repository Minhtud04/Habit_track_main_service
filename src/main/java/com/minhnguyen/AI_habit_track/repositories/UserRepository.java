package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email); // <-- THIS IS THE FIX
    //add
}
