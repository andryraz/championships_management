package api.bundesliga.dao.mapper;

import api.bundesliga.entity.Goal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GoalMapper {

    public static Goal apply(ResultSet rs) throws SQLException {
        return new Goal(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("match_id")),
                UUID.fromString(rs.getString("scorer_id")),
                UUID.fromString(rs.getString("club_id")),
                rs.getInt("minute_of_goal"),
                rs.getBoolean("own_goal")
        );
    }
}
