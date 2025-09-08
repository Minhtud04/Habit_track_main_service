package com.minhnguyen.AI_habit_track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
public class AiHabitTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiHabitTrackApplication.class, args);
	}

}
