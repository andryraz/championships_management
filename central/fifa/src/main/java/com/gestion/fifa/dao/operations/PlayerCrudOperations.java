package com.gestion.fifa.dao.operations;


import com.gestion.fifa.dao.DataSource;
import com.gestion.fifa.entity.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PlayerCrudOperations {
    private final DataSource dataSource;

    public List<Player> findAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM player_stats";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                players.add(new Player(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getInt("age"),
                        rs.getInt("scored_goals"),
                        rs.getLong("total_playing_time_seconds"),
                        rs.getString("championship_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }
}
