package com.gestion.fifa.dao.mapper;

import com.gestion.fifa.endpoint.rest.Club;
import com.gestion.fifa.endpoint.rest.ClubRanking;
import com.gestion.fifa.endpoint.rest.Coach;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClubMapper {

    public ClubRanking apply(ResultSet rs, int rank) {
        try {
            Club club = Club.builder()
                    .id(rs.getString("id"))
                    .name(rs.getString("name"))
                    .acronym(rs.getString("acronym"))
                    .yearCreation(rs.getInt("year_creation"))
                    .stadium(rs.getString("stadium"))
                    .coach(Coach.builder()
                            .name(rs.getString("coach_name"))
                            .nationality(rs.getString("coach_nationality"))
                            .build())
                    .build();

            return ClubRanking.builder()
                    .rank(rank)
                    .club(club)
                    .rankingPoints(rs.getInt("ranking_points"))
                    .scoredGoals(rs.getInt("goals_scored"))
                    .concededGoals(rs.getInt("goals_conceded"))
                    .differenceGoals(rs.getInt("goal_difference"))
                    .cleanSheetNumber(rs.getInt("clean_sheet_count"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to ClubRanking", e);
        }
    }
}
