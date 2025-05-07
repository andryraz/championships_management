package com.gestion.fifa.dao.operations;

import com.gestion.fifa.dao.DataSource;
import com.gestion.fifa.dao.mapper.PlayerMapper;
import com.gestion.fifa.entity.StatPlayer;
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
    private final DataSource ds;
    private final PlayerMapper mapper;

    public List<StatPlayer> findAll() {
        String sql = "SELECT id, name, number, nationality, age, position, scored_goals, total_playing_time_seconds, championship_name "
                + "FROM player_stats ORDER BY scored_goals DESC";
        List<StatPlayer> players = new ArrayList<>();

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                players.add(mapper.apply(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch players", e);
        }

        return players;
    }
}
