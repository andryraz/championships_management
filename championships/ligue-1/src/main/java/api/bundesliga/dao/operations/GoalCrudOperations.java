package api.bundesliga.dao.operations;


import api.bundesliga.dao.DataSource;
import api.bundesliga.entity.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GoalCrudOperations {
    private  final DataSource dataSource;
    public List<Goal> findByMatchId(UUID matchId) throws SQLException {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE match_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, matchId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                goals.add(new Goal(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("match_id")),
                        UUID.fromString(rs.getString("scorer_id")),
                        UUID.fromString(rs.getString("club_id")),
                        rs.getInt("minute_of_goal"),
                        rs.getBoolean("own_goal")
                ));
            }
        }
        return goals;
    }
}