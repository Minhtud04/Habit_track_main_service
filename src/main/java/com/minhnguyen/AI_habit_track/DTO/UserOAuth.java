package com.minhnguyen.AI_habit_track.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor     //constructor all args
@Getter
public class UserOAuth {
    private String username;
    private String email;
}
