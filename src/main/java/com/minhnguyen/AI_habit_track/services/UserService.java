package com.minhnguyen.AI_habit_track.services;
import com.minhnguyen.AI_habit_track.models.User;
import com.minhnguyen.AI_habit_track.repositories.UserRepository;
import com.minhnguyen.AI_habit_track.utils.ErrorException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public User addNewUser(User user) {
        //check match entity requirement:


        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ErrorException.ResourceAlreadyExists("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ErrorException.ResourceAlreadyExists("Email already exists");
        }

        // Save the new user to the database
        return userRepository.save(user);
    }
}
