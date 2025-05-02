package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.dao.mapper.ClubMapper;
import api.bundesliga.entity.Club;
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
public class ClubCrudOperations {
    private  final DataSource dataSource;
    private final ClubMapper clubMapper;

    public List<Club> findById(String id) {
        List<Club> clubs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select c.id, c.name, c.year_creation, c.acronym, c.stadium, c.coach_name, c.coach_nationality from player p join club c on p.club_id=c.id where p.id = ?::uuid")) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Club club = clubMapper.apply(resultSet);
                    clubs.add(club);
                }
                return clubs;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}