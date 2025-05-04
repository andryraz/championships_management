package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.entity.Match;
import api.bundesliga.entity.MatchStatus;
import api.bundesliga.entity.Scorer;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MatchCrudOperations {
    private  final DataSource dataSource;

    public List<Match> saveAll(List<Match> matches) {
        String sql = """
        INSERT INTO match (id, club_home_id, club_away_id, stadium, match_datetime, status, season_id)
        VALUES (?, ?, ?, ?, ?, ?::match_status, ?)
    """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Match match : matches) {
                UUID seasonId = findSeasonIdByYear(match.getSeasonYear(), conn);
                ps.setObject(1, UUID.fromString(match.getId()));
                ps.setObject(2, UUID.fromString(match.getClubPlayingHome().getId()));
                ps.setObject(3, UUID.fromString(match.getClubPlayingAway().getId()));
                ps.setString(4, match.getStadium());
                ps.setTimestamp(5, Timestamp.valueOf(match.getMatchDatetime().atStartOfDay())); // Convertir LocalDate
                ps.setString(6, match.getActualStatus().name());
                ps.setObject(7, seasonId);

                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting matches", e);
        }

        return matches;
    }

    public boolean existsBySeasonId(String seasonId) {
        String query = "SELECT COUNT(*) FROM match WHERE season_id = ?::uuid";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setObject(1, seasonId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de l'existence des matchs pour la saison", e);
        }
        return false;
    }

    public void insertGoal(UUID matchId, String clubId, Scorer scorer) {
        String query = "INSERT INTO goal (id, match_id, club_id, player_id, minute, own_goal) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, UUID.randomUUID());
            stmt.setObject(2, matchId);
            stmt.setObject(3, UUID.fromString(clubId));
            stmt.setObject(4, UUID.fromString(scorer.getPlayer().getId()));
            stmt.setInt(5, scorer.getMinuteOfGoal());
            stmt.setBoolean(6, scorer.getOwnGoal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du but", e);
        }
    }

    public Optional<MatchStatus> getMatchStatus(UUID matchId) {
        String query = "SELECT status FROM match WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, matchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(MatchStatus.valueOf(rs.getString("status")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du statut du match", e);
        }
    }


    private UUID findSeasonIdByYear(int seasonYear, Connection conn) throws SQLException {
        String sql = "SELECT id FROM season WHERE year = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seasonYear);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return UUID.fromString(rs.getString("id"));
            } else {
                throw new RuntimeException("No season found for year " + seasonYear);
            }
        }
    }

}
