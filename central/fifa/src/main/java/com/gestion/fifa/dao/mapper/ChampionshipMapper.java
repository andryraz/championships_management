package com.gestion.fifa.dao.mapper;

import com.gestion.fifa.entity.ChampionshipRanking;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

@Component
public class ChampionshipMapper {

    public ChampionshipRanking map(ResultSet rs, int rank) throws SQLException {
        ChampionshipRanking cr = new ChampionshipRanking();
        cr.setRank(rank);
        cr.setChampionship(rs.getString("championship"));
        cr.setDifferenceGoalsMedian(rs.getDouble("differenceGoalsMedian"));
        return cr;
    }
}
