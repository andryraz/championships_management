package com.gestion.fifa.dao.operations;


import com.gestion.fifa.dao.DataSource;
import com.gestion.fifa.dao.mapper.ChampionshipMapper;
import com.gestion.fifa.entity.ChampionshipRanking;
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
public class ChampionshipCrudOperations {
        private final DataSource dataSource;
        private final ChampionshipMapper mapper;

    public List<ChampionshipRanking> findAllRankings() {
        List<ChampionshipRanking> rankings = new ArrayList<>();
        String sql = "SELECT name, goal_difference_median FROM championship_stats";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rankings.add(mapper.apply(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load championship rankings", e);
        }

        return rankings;
    }
    }


