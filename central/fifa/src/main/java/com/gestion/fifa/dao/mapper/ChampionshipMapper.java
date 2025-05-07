package com.gestion.fifa.dao.mapper;

import com.gestion.fifa.entity.ChampionshipRanking;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Component
public class ChampionshipMapper implements Function<ResultSet, ChampionshipRanking> {

    @Override
    public ChampionshipRanking apply(ResultSet rs) {
        try {
            return new ChampionshipRanking(
                    0,
                    rs.getString("name"),
                    rs.getDouble("goal_difference_median")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to ChampionshipRanking", e);
        }
    }
}

