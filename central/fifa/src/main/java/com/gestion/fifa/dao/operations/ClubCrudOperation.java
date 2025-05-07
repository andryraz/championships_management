package com.gestion.fifa.dao.operations;

import com.gestion.fifa.dao.DataSource;
import com.gestion.fifa.dto.Club;
import com.gestion.fifa.dto.ClubRanking;
import com.gestion.fifa.dto.Coach;
import com.gestion.fifa.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubCrudOperation {

    private final DataSource dataSource;

    public List<ClubRanking> findTopClubs(int top) {
        List<ClubRanking> rankings = new ArrayList<>();

        String query = """
            SELECT * FROM club_stats
            ORDER BY goal_difference DESC, goals_scored DESC
            LIMIT ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, top);
            ResultSet rs = stmt.executeQuery();

            int rank = 1;
            while (rs.next()) {
                Club club = Club.builder()
                        .id(rs.getString("id"))
                        .name(rs.getString("name"))
                        .acronym(rs.getString("acronym"))
                        .yearCreation(0) // Valeur par défaut
                        .stadium("")     // Valeur par défaut
                        .coach(Coach.builder()
                                .name(rs.getString("coach_name"))
                                .nationality(rs.getString("nationality"))
                                .build())
                        .build();

                ClubRanking clubRanking = ClubRanking.builder()
                        .rank(rank++)
                        .club(club)
                        .rankingPoints(0) // Par défaut (non disponible)
                        .scoredGoals(rs.getInt("goals_scored"))
                        .concededGoals(rs.getInt("goals_conceded"))
                        .differenceGoals(rs.getInt("goal_difference"))
                        .cleanSheetNumber(rs.getInt("clean_sheet_count"))
                        .build();

                rankings.add(clubRanking);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rankings;
    }
}
