package com.gestion.fifa.dao.operations;

import com.gestion.fifa.dao.DataSource;
import com.gestion.fifa.dto.PlayerPosition;
import com.gestion.fifa.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PlayerCrudOperations {
    private final DataSource ds;



    public List<StatPlayer> findAll() {
        String sql = "SELECT id, name, number, nationality, age, position, scored_goals, total_playing_time_seconds, championship_name "
                + "FROM player_stats";
        List<StatPlayer> list = new ArrayList<>();
        try (Connection c = ds.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {
            while (rs.next()) {
                StatPlayer p = StatPlayer.builder()
                        .id(rs.getObject("id", UUID.class).toString())
                        .name(rs.getString("name"))
                        .number(rs.getInt("number"))
                        .nationality(rs.getString("nationality"))
                        .age(rs.getInt("age"))
                        .playerPosition(PlayerPosition.valueOf(rs.getString("position")))
                        .scored_goals(rs.getInt("scored_goals"))
                        .playing_time_seconds(rs.getInt("total_playing_time_seconds"))
                        .ChampionshipName(rs.getString("championship_name"))
                        .build();
                list.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
