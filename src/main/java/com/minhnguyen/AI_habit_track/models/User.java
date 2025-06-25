package com.minhnguyen.AI_habit_track.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   //Getter & Setter
@AllArgsConstructor     //constructor all args
@NoArgsConstructor
@Entity                 //Database entity
@Table(name = "users") // Specify table name
//email unique

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // --- Username Constraints ---
    @NotBlank(message = "Username cannot be empty") // Application-level: must not be null or just whitespace
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") // Application-level: character limit
    @Column(nullable = false, unique = true, length = 50) // Database-level: NOT NULL, UNIQUE, and VARCHAR(50)
    private String username;

    // --- Email Constraints ---
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email format") // Application-level: checks for email pattern
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100) // Database-level: NOT NULL, UNIQUE, VARCHAR(100)
    private String email;

}
