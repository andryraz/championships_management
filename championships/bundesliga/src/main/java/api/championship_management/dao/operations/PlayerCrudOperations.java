package api.championship_management.dao.operations;


import api.championship_management.dao.DataSource;
import api.championship_management.dao.mapper.PlayerMapper;
import api.championship_management.model.player;
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
public class PlayerCrudOperations {
    private  DataSource dataSource;
    private PlayerMapper playerMapper;

    public List<player> getAll(int page, int size) {
        List<player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, name, number, position, nationality FROM player ORDER BY id ASC LIMIT ? OFFSET ?")) {

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
}
