package com.gestion.fifa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private UUID id;
    private String name;
    private String position;
    private int age;
    private int scoredGoals;
    private long totalPlayingTimeSeconds;
    private String championshipName;
}