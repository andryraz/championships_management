// ClubRanking.java
package com.gestion.fifa.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubRanking {
    private int rank;
    private Club club;
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
}
