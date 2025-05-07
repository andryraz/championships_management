package com.gestion.fifa.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRanking {
    private int rank;
    private String id;
    private String name;
    private int number;
    private String position;
    private String nationality;
    private int age;
    private String championship;
    private int scoredGoals;
    private PlayingTime playingTime;
}
