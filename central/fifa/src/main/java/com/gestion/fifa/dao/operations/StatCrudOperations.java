package com.gestion.fifa.dao.operations;


import com.gestion.fifa.dao.DataSource;

import com.gestion.fifa.entity.StatClub;
import com.gestion.fifa.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StatCrudOperations {
    private final DataSource dataSource;

    public void deletePlayerStats() {
        String sql = "DELETE FROM player_stats";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete player stats", e);
        }
    }

    public void deleteClubStats() {
        String sql = "DELETE FROM club_stats";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete club stats", e);
        }
    }


    public void insertPlayerStats(List<StatPlayer> playerStats) {
        String sql = """
            INSERT INTO player_stats (id, name, position, age, scored_goals, total_playing_time_seconds, championship_name, number, nationality)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (StatPlayer player : playerStats) {
                stmt.setObject(1, UUID.fromString(player.getId()));
                stmt.setString(2, player.getName());
                stmt.setString(3, player.getPlayerPosition().name());
                stmt.setInt(4, player.getAge());
                stmt.setInt(5, player.getScored_goals());
                stmt.setLong(6, player.getPlaying_time_seconds());
                stmt.setString(7, player.getChampionshipName());
                stmt.setInt(8, player.getNumber());
                stmt.setString(9, player.getNationality());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert player stats", e);
        }
    }


    public void insertClubStats(List<StatClub> clubStats) {
        String sql = """
            INSERT INTO club_stats (id, name, acronym, championship_name, goals_scored, goals_conceded, goal_difference, clean_sheet_count, coach_name, coach_nationality, year_creation, stadium, ranking_points)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (StatClub club : clubStats) {
                stmt.setObject(1, UUID.randomUUID());
                stmt.setString(2, club.getName());
                stmt.setString(3, club.getAcronym());
                stmt.setString(4, club.getChampionshipName());
                stmt.setInt(5, club.getScored_goals());
                stmt.setInt(6, club.getConceded_goals());
                stmt.setInt(7, club.getDifference_goals());
                stmt.setInt(8, club.getCleanSheetNumber());
                stmt.setString(9, club.getCoach().getName());
                stmt.setString(10, club.getCoach().getNationality());
                stmt.setInt(11, club.getYear_creation());
                stmt.setString(12, club.getStadium());
                stmt.setInt(13, club.getRanking_points());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert club stats", e);
        }
    }

    public void insertChampionshipMedian(String championshipName, double median) {
        String sql = """
        INSERT INTO championship_stats (name, goal_difference_median)
        VALUES (?, ?)
        ON CONFLICT (name) DO UPDATE SET goal_difference_median = EXCLUDED.goal_difference_median
    """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, championshipName);
            stmt.setDouble(2, median);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert/update median for championship: " + championshipName, e);
        }
    }
    public List<Integer> findGoalDifferencesByChampionship(String championshipName) {
        List<Integer> values = new ArrayList<>();
        String sql = "SELECT goal_difference FROM club_stats WHERE championship_name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, championshipName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                values.add(rs.getInt("goal_difference"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read club stats for championship: " + championshipName, e);
        }
        return values;
    }


}
