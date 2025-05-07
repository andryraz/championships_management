package com.gestion.fifa.dao.operations;

import com.gestion.fifa.dao.DataSource;
import com.gestion.fifa.dao.mapper.ClubMapper;
import com.gestion.fifa.endpoint.rest.ClubRanking;
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
public class ClubCrudOperation {

    private final DataSource dataSource;
    private final ClubMapper mapper;

    public List<ClubRanking> findTopClubs(int top) {
        List<ClubRanking> rankings = new ArrayList<>();

        String query = """
            SELECT * FROM club_stats
            ORDER BY ranking_points DESC, goal_difference DESC, clean_sheet_count DESC
            LIMIT ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, top);
            ResultSet rs = stmt.executeQuery();

            int rank = 1;
            while (rs.next()) {
                rankings.add(mapper.apply(rs, rank++));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch top clubs", e);
        }

        return rankings;
    }
}
