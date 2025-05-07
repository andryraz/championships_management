package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.entity.ClubStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClubStatisticsCrudOperations {
    private  final DataSource dataSource;

    public ClubStatistics findBySeasonAndClub(UUID seasonId, UUID clubId) throws SQLException {
        String sql = "SELECT * FROM club_statistics WHERE season_id = ? AND club_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, seasonId);
            ps.setObject(2, clubId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ClubStatistics(
                        UUID.fromString(rs.getString("id")),
                        seasonId,
                        clubId,
                        rs.getInt("ranking_points"),
                        rs.getInt("scored_goals"),
                        rs.getInt("conceded_goals"),
                        rs.getInt("difference_goals"),
                        rs.getInt("clean_sheet_number")
                );
            } else {
                return new ClubStatistics(UUID.randomUUID(), seasonId, clubId, 0, 0, 0, 0, 0);
            }
        }
    }

    public void save(ClubStatistics stats) throws SQLException {
        String sql = """
            INSERT INTO club_statistics (id, season_id, club_id, ranking_points, scored_goals, conceded_goals, difference_goals, clean_sheet_number)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (season_id, club_id) DO UPDATE SET
                ranking_points = EXCLUDED.ranking_points,
                scored_goals = EXCLUDED.scored_goals,
                conceded_goals = EXCLUDED.conceded_goals,
                difference_goals = EXCLUDED.difference_goals,
                clean_sheet_number = EXCLUDED.clean_sheet_number
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, stats.getId());
            ps.setObject(2, stats.getSeasonId());
            ps.setObject(3, stats.getClubId());
            ps.setInt(4, stats.getRankingPoints());
            ps.setInt(5, stats.getScoredGoals());
            ps.setInt(6, stats.getConcededGoals());
            ps.setInt(7, stats.getDifferenceGoals());
            ps.setInt(8, stats.getCleanSheetNumber());
            ps.executeUpdate();
        }
    }
}

