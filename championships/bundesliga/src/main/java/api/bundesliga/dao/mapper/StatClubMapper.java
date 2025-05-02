package api.bundesliga.dao.mapper;

import api.bundesliga.entity.Player;
import api.bundesliga.entity.PlayerPosition;
import api.bundesliga.entity.StatClub;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StatClubMapper implements  Function<ResultSet, StatClub>{
    @SneakyThrows
    @Override
    public StatClub apply(ResultSet resultSet) {
        //String id = resultSet.getString("id");

        StatClub statClub = new StatClub();
        statClub.setId(resultSet.getString("id"));
        statClub.setName(resultSet.getString("name"));
        statClub.setAcronym(resultSet.getString("acronym"));
        statClub.setYear_creation(resultSet.getInt("year_creation"));
        statClub.setStadium(resultSet.getString("stadium"));
        statClub.setCoach_name(resultSet.getString("coach_name"));
        statClub.setCoach_nationality(resultSet.getString("coach_nationality"));
        statClub.setRanking_points(resultSet.getInt("ranking_points"));
        statClub.setScored_goals(resultSet.getInt("scored_goals"));
        statClub.setConceded_goals(resultSet.getInt("conceded_goals"));
        statClub.setDifference_goals(resultSet.getInt("difference_goals"));
        statClub.setCleanSheetNumber(resultSet.getInt("clean_sheet_number"));

        return statClub;
    }

}
