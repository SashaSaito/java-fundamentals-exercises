package com.bobocode.intro;

import java.util.Base64;

import com.bobocode.util.ExerciseNotCompletedException;

public class ExerciseIntroduction {

    public String getWelcomeMessage() {
        return "The key to efficient learning is practice!";
    }

    public String encodeMessage(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
    }
}
