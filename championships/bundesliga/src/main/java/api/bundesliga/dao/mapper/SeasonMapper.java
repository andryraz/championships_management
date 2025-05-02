package api.bundesliga.dao.mapper;

import api.bundesliga.dao.operations.ClubCrudOperations;
import api.bundesliga.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class SeasonMapper implements Function<ResultSet, Season> {

    @SneakyThrows
    @Override
    public Season apply(ResultSet resultSet) {
        Season season = new Season();

        season.setId(resultSet.getString("id"));
        season.setYear(resultSet.getInt("year"));
        season.setAlias(resultSet.getString("alias"));

        season.setStatus(SeasonStatus.valueOf(resultSet.getString("status")));
        return season;
    }
}
