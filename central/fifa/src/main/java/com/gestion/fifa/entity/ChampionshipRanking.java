package com.gestion.fifa.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChampionshipRanking {
        private int rank;
        private String championship;
        private double differenceGoalsMedian;
}
