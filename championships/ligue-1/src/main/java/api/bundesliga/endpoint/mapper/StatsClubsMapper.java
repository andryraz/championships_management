package api.bundesliga.endpoint.mapper;

import api.bundesliga.endpoint.rest.StatClubRest;
import api.bundesliga.endpoint.rest.StatPlayerRest;
import api.bundesliga.entity.Coach;
import api.bundesliga.entity.StatClub;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StatsClubsMapper implements Function<ResultSet, StatClubRest> {
    @SneakyThrows
    @Override
    public StatClubRest apply(ResultSet resultSet) {
        StatClubRest statClub = new StatClubRest();
        statClub.setId(resultSet.getString("id"));
        statClub.setName(resultSet.getString("name"));
        statClub.setAcronym(resultSet.getString("acronym"));
        statClub.setYear_creation(resultSet.getInt("year_creation"));
        statClub.setStadium(resultSet.getString("stadium"));

        Coach coach = Coach.builder()
                .name(resultSet.getString("coach_name"))
                .nationality(resultSet.getString("coach_nationality"))
                .build();
        statClub.setCoach(coach);

        statClub.setRanking_points(resultSet.getInt("ranking_points"));
        statClub.setScored_goals(resultSet.getInt("scored_goals"));
        statClub.setConceded_goals(resultSet.getInt("conceded_goals"));
        statClub.setDifference_goals(resultSet.getInt("difference_goals"));
        statClub.setCleanSheetNumber(resultSet.getInt("clean_sheet_number"));
        statClub.setChampionshipName("ligue-1");

        return statClub;
    }
}
