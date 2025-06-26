package com.minhnguyen.AI_habit_track.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor     //constructor all args
@Getter
public class UserOAuthDTO {
    private String username;
    private String email;
}
