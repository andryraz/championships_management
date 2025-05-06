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

        public List<ChampionshipRanking> findAllOrderedByMedianAsc() {
            String sql = """
            SELECT name AS championship, goal_difference_median AS differenceGoalsMedian
            FROM championship_stats
            ORDER BY goal_difference_median ASC
        """;

            List<ChampionshipRanking> rankings = new ArrayList<>();

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                int rank = 1;
                while (rs.next()) {
                    rankings.add(mapper.map(rs, rank++));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return rankings;
        }
    }


