package com.minhnguyen.AI_habit_track.repositories;

import com.minhnguyen.AI_habit_track.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByUserId(Long userId);

    User findByEmail(String email);
    //add

}
