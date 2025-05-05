package com.gestion.fifa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerRanking {
    private String name;
    private int scoredGoals;
    private long playingTime; // Converted in the requested unit
    private String championship;
}