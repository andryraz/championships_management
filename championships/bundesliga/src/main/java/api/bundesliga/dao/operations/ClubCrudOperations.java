package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.dao.mapper.ClubMapper;
import api.bundesliga.dao.mapper.PlayerMapper;
import api.bundesliga.dao.mapper.StatClubMapper;
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

@Repository

@RequiredArgsConstructor
public class ClubCrudOperations {
    private  final DataSource dataSource;
    private final ClubMapper clubMapper;
    private final StatClubMapper statClubMapper;
    private final PlayerMapper playerMapper;

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
}