package api.bundesliga.dao.mapper;


import api.bundesliga.entity.Club;
import api.bundesliga.entity.Coach;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class ClubMapper implements Function<ResultSet, Club> {

    @SneakyThrows
    @Override
    public Club apply(ResultSet resultSet) {

        Coach coach = Coach.builder()
                .name(resultSet.getString("coach_name"))
                .nationality(resultSet.getString("coach_nationality"))
                .build();

        return Club.builder()
                .id(resultSet.getString("id"))
                .name(resultSet.getString("name"))
                .year_creation(resultSet.getInt("year_creation"))
                .acronym(resultSet.getString("acronym"))
                .stadium(resultSet.getString("stadium"))
                .coach(coach)
                .build();
    }
}

