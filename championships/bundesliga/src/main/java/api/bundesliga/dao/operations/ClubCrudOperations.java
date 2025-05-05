package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.dao.mapper.ClubMapper;
import api.bundesliga.dao.mapper.PlayerMapper;
import api.bundesliga.dao.mapper.StatClubMapper;
import api.bundesliga.endpoint.mapper.PlayerRestMapper;
import api.bundesliga.entity.Club;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.StatClub;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
public class ClubCrudOperations {
    private  final DataSource dataSource;
    private final ClubMapper clubMapper;
    private final StatClubMapper statClubMapper;
    private final PlayerMapper playerMapper;
    private final PlayerRestMapper playerRestMapper;

    public List<Club> getAll(int page, int size) {
        List<Club> clubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT c.id, c.name, c.year_creation, c.acronym, c.stadium, c.coach_name, c.coach_nationality FROM club c ORDER BY id ASC LIMIT ? OFFSET ?")) {
            statement.setInt(1, size);
            statement.setInt(2, (page - 1) * size);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    clubs.add(clubMapper.apply(resultSet));
                }
            }
            return clubs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public List<Club> saveAll(List<Club> entities) {
        List<Club> clubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into club (id, name, acronym, year_creation, stadium, coach_name, coach_nationality) values (?::uuid, ?, ?, ?, ?, ?, ?)"
                                 + " on conflict (id) do update set id=excluded.id, name=excluded.name, acronym=excluded.acronym, year_creation=excluded.year_creation, stadium=excluded.stadium, coach_name=excluded.coach_name, coach_nationality=excluded.coach_nationality"
                                 + " returning id, name, acronym, year_creation, stadium, coach_name, coach_nationality")) {
                entities.forEach(entityToSave -> {
                    try {
                        statement.setString(1, entityToSave.getId());
                        statement.setString(2, entityToSave.getName());
                        statement.setString(3, entityToSave.getAcronym());
                        statement.setInt(4, entityToSave.getYear_creation());
                        statement.setString(5, entityToSave.getStadium());
                        statement.setString(6, entityToSave.getCoach_name());
                        statement.setString(7, entityToSave.getCoach_nationality());

                        statement.addBatch();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        clubs.add(clubMapper.apply(resultSet));
                    }
                }
                return clubs;
            }
        }
    }

    public List<StatClub> getStatForSpecificSeason (int seasonYear) {
        List<StatClub> statclubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT c.id, c.name, c.acronym, c.stadium, c.year_creation, c.coach_name, c.coach_nationality, cs.ranking_points, cs.scored_goals, cs.conceded_goals, cs.difference_goals, cs.clean_sheet_number FROM club_statistics cs JOIN club c ON c.id = cs.club_id JOIN season s ON s.id = cs.season_id WHERE s.year = ?")) {
            statement.setInt(1, seasonYear);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    statclubs.add(statClubMapper.apply(resultSet));
                }
            }
            return statclubs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Club> findById(String id) throws RuntimeException {
        List<Club> clubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select c.id, c.name, c.year_creation, c.acronym, c.stadium, c.coach_name, c.coach_nationality from player p join club c on p.club_id=c.id where p.id = ?::uuid")) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Club club = clubMapper.apply(resultSet);
                    clubs.add(club);
                }
                return clubs;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Player> findPlayerByIdClub(String id) throws RuntimeException {
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select p.id, p.name, p.number, p.position, p.age, p.nationality from player p join club c on p.club_id=c.id where club_id  = ?::uuid")) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Player player = playerMapper.apply(resultSet);
                    players.add(player);
                }
                return players;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsClubById(String clubId) {
        String sql = "SELECT 1 FROM club WHERE id = ?::uuid";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clubId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to verify club existence", e);
        }
    }
    public boolean existsPlayerInAnotherClub(String playerId, String currentClubId) {
        String sql = "SELECT club_id FROM player WHERE id = ?::uuid";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String clubId = rs.getString("club_id");
                return clubId != null && !clubId.equals(currentClubId);
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check player club association", e);
        }
    }

    public void detachAllPlayersFromClub(String clubId) {
        String sql = "UPDATE player SET club_id = NULL WHERE club_id = ?::uuid";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clubId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to detach players from club", e);
        }
    }

    public void saveOrUpdatePlayer(Player p) {
        String sql = "INSERT INTO player (id, name, number, age, nationality, position, club_id) " +
                "VALUES (?::uuid, ?, ?, ?, ?, ?::player_position, ?::uuid) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "name = EXCLUDED.name, number = EXCLUDED.number, age = EXCLUDED.age, " +
                "nationality = EXCLUDED.nationality, position = EXCLUDED.position, club_id = EXCLUDED.club_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (p.getId() == null) {
                p.setId(UUID.randomUUID().toString());
            }

            stmt.setString(1, p.getId());
            stmt.setString(2, p.getName());
            stmt.setInt(3, p.getNumber());
            stmt.setInt(4, p.getAge());
            stmt.setString(5, p.getNationality());
            stmt.setString(6, p.getPlayerPosition().name());
            stmt.setString(7, p.getClub().getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save or update player", e);
        }
    }

    public List<StatClub> getStat () {
        List<StatClub> statclubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT c.id, c.name, c.acronym, c.stadium, c.year_creation, c.coach_name, c.coach_nationality, cs.ranking_points, cs.scored_goals, cs.conceded_goals, cs.difference_goals, cs.clean_sheet_number FROM club_statistics cs JOIN club c ON c.id = cs.club_id JOIN season s ON s.id = cs.season_id")) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    statclubs.add(statClubMapper.apply(resultSet));
                }
            }
            return statclubs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}