package api.bundesliga.dao.operations;


import api.bundesliga.dao.DataSource;
import api.bundesliga.entity.Player;
import api.bundesliga.dao.mapper.PlayerMapper;
import api.bundesliga.dao.mapper.StatPlayerMapper;
//import api.bundesliga.dao.mapper.Player;
import api.bundesliga.entity.StatPlayer;
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
public class PlayerCrudOperations {
    private  final DataSource dataSource;
    private final PlayerMapper playerMapper;
    private final StatPlayerMapper statplayerMapper;

    public List<Player> getAll(int page, int size) {
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, name, number, position, nationality, position, age FROM player ORDER BY id ASC LIMIT ? OFFSET ?")) {
            statement.setInt(1, size);
            statement.setInt(2, (page - 1) * size);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    players.add(playerMapper.apply(resultSet));
                }
            }
            return players;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public List<Player> saveAll(List<Player> entities) {
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into player (id, name, number, position, age, nationality) values (?::uuid, ?, ?, ?::player_position, ?, ?)"
                                 + " on conflict (id) do update set id=excluded.id, name=excluded.name, number=excluded.number, position=excluded.position, age=excluded.age, nationality=excluded.nationality"
                                 + " returning id, name, number, position, age, nationality")) {
                entities.forEach(entityToSave -> {
                    try {
                        statement.setString(1, entityToSave.getId());
                        statement.setString(2, entityToSave.getName());
                        statement.setInt(3, entityToSave.getNumber());
                        statement.setString(4, entityToSave.getPlayerPosition().name());
                        statement.setInt(5, entityToSave.getAge());
                        statement.setString(6, entityToSave.getNationality());
                        statement.addBatch(); // group by batch so executed as one query in database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        players.add(playerMapper.apply(resultSet));
                    }
                }
                return players;
            }
        }
    }

    public StatPlayer findByIdPlayer(String player_id, Integer seasonYear) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT ps.scored_goals, ps.playing_time_seconds FROM player_statistics ps JOIN player p on ps.player_id = p.id JOIN season s on s.id=ps.season_id where ps.player_id= ?::uuid and s.year= ?")) {
            statement.setString(1, player_id);
            statement.setInt(2, seasonYear);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return statplayerMapper.apply(resultSet);
                }
            }
            throw new RuntimeException("player.id=" + player_id  +  " not found or" + seasonYear + "not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
