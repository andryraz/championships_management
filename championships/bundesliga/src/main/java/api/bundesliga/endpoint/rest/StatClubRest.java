package api.bundesliga.endpoint.rest;

import api.bundesliga.entity.Coach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StatClubRest {

    private String id;
    private String name;
    private String acronym;
    private int year_creation;
    private String stadium;
    private Coach coach;
//    private String coach_name;
//    private String coach_nationality;
    private int ranking_points;
    private int scored_goals;
    private int conceded_goals;
    private int difference_goals;
    private int cleanSheetNumber;
    private String ChampionshipName;
}
