package com.minhnguyen.AI_habit_track.services.sub_services;

import com.minhnguyen.AI_habit_track.DTO.UserOAuthDTO;
import com.minhnguyen.AI_habit_track.models.User;
import com.minhnguyen.AI_habit_track.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by email or creates a new one if not found.
     * This prevents creating duplicate users on every login or request.
     *
     * @param userDto The DTO containing user details from the auth provider.
     * @return The persisted User entity.
     */
    @Transactional
    public User findOrCreateUser(UserOAuthDTO userDto) {
        if (userDto == null || userDto.getEmail() == null) {
            throw new IllegalArgumentException("User details cannot be null");
        }

        return userRepository.findByEmail(userDto.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(userDto.getUsername());
                    newUser.setEmail(userDto.getEmail());
                    return userRepository.save(newUser);
                });
    }
}