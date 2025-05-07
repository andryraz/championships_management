package com.gestion.fifa.entity;


import com.gestion.fifa.endpoint.rest.Coach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StatClub {
    private String id;
    private String name;
    private String acronym;
    private int year_creation;
    private String stadium;
    private Coach coach;
    private int ranking_points;
    private int scored_goals;
    private int conceded_goals;
    private int difference_goals;
    private int cleanSheetNumber;
    private String ChampionshipName;
}

