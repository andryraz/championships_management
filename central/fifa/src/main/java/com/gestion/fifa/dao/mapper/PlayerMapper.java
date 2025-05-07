package com.gestion.fifa.dao.mapper;

import com.gestion.fifa.dto.PlayerPosition;
import com.gestion.fifa.entity.StatPlayer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

@Component
public class PlayerMapper implements Function<ResultSet, StatPlayer> {

    @Override
    public StatPlayer apply(ResultSet rs) {
        try {
            return StatPlayer.builder()
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
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to StatPlayer", e);
        }
    }
}
