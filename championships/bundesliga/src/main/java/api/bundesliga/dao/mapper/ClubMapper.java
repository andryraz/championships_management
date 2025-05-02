package api.bundesliga.dao.mapper;


import api.bundesliga.entity.Club;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class ClubMapper implements Function<ResultSet, Club> {

    @SneakyThrows
    @Override
    public Club apply(ResultSet resultSet) {

        Club club = new Club();
        club.setId(resultSet.getString("id"));
        club.setName(resultSet.getString("name"));
        club.setYear_creation(resultSet.getInt("year_creation"));
        club.setAcronym(resultSet.getString("acronym"));
        club.setStadium(resultSet.getString("stadium"));
        club.setCoach_name(resultSet.getString("coach_name"));
        club.setCoach_nationality(resultSet.getString("coach_nationality"));

        return club;
    }
}
