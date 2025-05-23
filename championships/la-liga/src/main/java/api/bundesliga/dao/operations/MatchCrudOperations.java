package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public MatchEntity findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM match WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MatchEntity(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("club_home_id")),
                        UUID.fromString(rs.getString("club_away_id")),
                        rs.getString("stadium"),
                        rs.getTimestamp("match_datetime").toLocalDateTime(),
                        MatchStatus.valueOf(rs.getString("status")),
                        UUID.fromString(rs.getString("season_id"))
                );
            }
            return null;
        }
    }

    public void updateStatus(UUID id, MatchStatus newStatus) throws SQLException {
        String sql = "UPDATE match SET status = ?::match_status WHERE id = ?::uuid";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setObject(2, id);
            ps.executeUpdate();
        }
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
        String query = "INSERT INTO goal (id, match_id, club_id, scorer_id, minute_of_goal, own_goal) VALUES (?, ?::uuid, ?, ?, ?, ?)";
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

    public List<Match> findBySeasonWithFilters(int seasonYear,
                                               MatchStatus status,
                                               String clubName,
                                               LocalDate after,
                                               LocalDate beforeOrEquals) {
        StringBuilder sql = new StringBuilder("""
            SELECT m.id, m.stadium, m.match_datetime, m.status,
                   ch.id AS home_id, ch.name AS home_name, ch.acronym AS home_acr,
                   ca.id AS away_id, ca.name AS away_name, ca.acronym AS away_acr
              FROM match m
              JOIN club ch ON m.club_home_id = ch.id
              JOIN club ca ON m.club_away_id = ca.id
              JOIN season s ON m.season_id = s.id
             WHERE s.year = ?
        """);

        List<Object> params = new ArrayList<>();
        params.add(seasonYear);

        if (status != null) {
            sql.append(" AND m.status = ?::match_status");
            params.add(status.name());
        }
        if (clubName != null && !clubName.isBlank()) {
            sql.append(" AND (ch.name ILIKE ? OR ca.name ILIKE ?)");
            String pattern = "%" + clubName + "%";
            params.add(pattern);
            params.add(pattern);
        }
        if (after != null) {
            sql.append(" AND m.match_datetime::date > ?");
            params.add(Date.valueOf(after));
        }
        if (beforeOrEquals != null) {
            sql.append(" AND m.match_datetime::date <= ?");
            params.add(Date.valueOf(beforeOrEquals));
        }

        sql.append(" ORDER BY m.match_datetime");

        List<Match> matches = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // bind parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Match m = Match.builder()
                            .id(rs.getString("id"))
                            .stadium(rs.getString("stadium"))
                            .matchDatetime(rs.getTimestamp("match_datetime").toLocalDateTime().toLocalDate())
                            .actualStatus(MatchStatus.valueOf(rs.getString("status")))
                            .seasonYear(seasonYear)
                            .clubPlayingHome(
                                    MatchClub.builder()
                                            .id(rs.getString("home_id"))
                                            .name(rs.getString("home_name"))
                                            .acronym(rs.getString("home_acr"))
                                            .build()
                            )
                            .clubPlayingAway(
                                    MatchClub.builder()
                                            .id(rs.getString("away_id"))
                                            .name(rs.getString("away_name"))
                                            .acronym(rs.getString("away_acr"))
                                            .build()
                            )
                            .build();


                    m.setClubPlayingHome(loadScorers(conn, m.getId(), m.getClubPlayingHome()));
                    m.setClubPlayingAway(loadScorers(conn, m.getId(), m.getClubPlayingAway()));

                    matches.add(m);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur accès données matches", e);
        }

        return matches;
    }

    private MatchClub loadScorers(Connection conn, String matchId, MatchClub club) throws SQLException {
        String sql = """
            SELECT g.scorer_id, g.minute_of_goal, g.own_goal,
                   p.name AS player_name, p.number AS player_number
              FROM goal g
              JOIN player p ON g.scorer_id = p.id
             WHERE g.match_id = ? AND g.club_id = ?
             ORDER BY g.minute_of_goal
        """;
        List<Scorer> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(matchId));
            ps.setObject(2, UUID.fromString(club.getId()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Scorer sc = Scorer.builder()
                            .player(PlayerMin.builder()
                                    .id(rs.getString("scorer_id"))
                                    .name(rs.getString("player_name"))
                                    .number(rs.getInt("player_number"))
                                    .build()
                            )
                            .minuteOfGoal(rs.getInt("minute_of_goal"))
                            .ownGoal(rs.getBoolean("own_goal"))
                            .build();
                    list.add(sc);
                }
            }
        }
        club.setScorers(list);
        return club;
    }

    public Optional<Match> getMatchByIdWithDetails(UUID matchId) {
        String matchQuery = """
        SELECT m.id as match_id, m.stadium, m.match_datetime, m.status, s.year as season_year,
               home.id as home_id, home.name as home_name, home.acronym as home_acronym,
               away.id as away_id, away.name as away_name, away.acronym as away_acronym
        FROM match m
        JOIN club home ON m.club_home_id = home.id
        JOIN club away ON m.club_away_id = away.id
        JOIN season s ON m.season_id = s.id
        WHERE m.id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement matchStmt = conn.prepareStatement(matchQuery)) {

            matchStmt.setObject(1, matchId);
            ResultSet rs = matchStmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            MatchClub home = MatchClub.builder()
                    .id(rs.getString("home_id"))
                    .name(rs.getString("home_name"))
                    .acronym(rs.getString("home_acronym"))
                    .score(getClubScore(conn, matchId, rs.getString("home_id")))
                    .scorers(getScorers(conn, matchId, rs.getString("home_id")))
                    .build();

            MatchClub away = MatchClub.builder()
                    .id(rs.getString("away_id"))
                    .name(rs.getString("away_name"))
                    .acronym(rs.getString("away_acronym"))
                    .score(getClubScore(conn, matchId, rs.getString("away_id")))
                    .scorers(getScorers(conn, matchId, rs.getString("away_id")))
                    .build();

            return Optional.of(Match.builder()
                    .id(rs.getString("match_id"))
                    .stadium(rs.getString("stadium"))
                    .matchDatetime(rs.getDate("match_datetime").toLocalDate())
                    .actualStatus(MatchStatus.valueOf(rs.getString("status")))
                    .seasonYear(rs.getInt("season_year"))
                    .clubPlayingHome(home)
                    .clubPlayingAway(away)
                    .build());

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du match", e);
        }
    }

    private Integer getClubScore(Connection conn, UUID matchId, String clubId) throws SQLException {
        String query = "SELECT COUNT(*) FROM goal WHERE match_id = ? AND club_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, matchId);
            stmt.setObject(2, UUID.fromString(clubId));
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private List<Scorer> getScorers(Connection conn, UUID matchId, String clubId) throws SQLException {
        String query = """
        SELECT g.minute_of_goal, g.own_goal, p.id, p.name, p.number
        FROM goal g
        JOIN player p ON g.scorer_id = p.id
        WHERE g.match_id = ? AND g.club_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, matchId);
            stmt.setObject(2, UUID.fromString(clubId));
            ResultSet rs = stmt.executeQuery();

            List<Scorer> scorers = new ArrayList<>();
            while (rs.next()) {
                PlayerMin player = new PlayerMin(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("number")
                );
                Scorer scorer = new Scorer(player, rs.getInt("minute_of_goal"), rs.getBoolean("own_goal"));
                scorers.add(scorer);
            }
            return scorers;
        }
    }





}
