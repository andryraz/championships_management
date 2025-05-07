package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.dao.mapper.GoalMapper;
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
    private final DataSource dataSource;

    public List<Goal> findByMatchId(UUID matchId) throws SQLException {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goal WHERE match_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, matchId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                goals.add(GoalMapper.apply(rs));
            }
        }
        return goals;
    }
}
