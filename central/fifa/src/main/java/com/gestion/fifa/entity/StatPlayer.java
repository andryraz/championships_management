package com.gestion.fifa.entity;

import com.gestion.fifa.endpoint.rest.PlayerPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StatPlayer {
    private String id;
    private String name;
    private Integer number;
    private String nationality;
    private Integer age;
    private PlayerPosition playerPosition;
    private int scored_goals;
    private int playing_time_seconds;
    private String ChampionshipName;
}