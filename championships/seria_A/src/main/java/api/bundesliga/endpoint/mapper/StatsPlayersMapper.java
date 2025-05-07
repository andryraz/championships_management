package api.bundesliga.endpoint.mapper;


import api.bundesliga.endpoint.rest.StatPlayerRest;
import api.bundesliga.entity.PlayerPosition;
import api.bundesliga.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StatsPlayersMapper implements Function<ResultSet, StatPlayerRest> {

    @SneakyThrows
    @Override
    public StatPlayerRest apply(ResultSet resultSet) {

        StatPlayerRest player = new StatPlayerRest();
        player.setId(resultSet.getString("id"));
        player.setName(resultSet.getString("name"));
        player.setPlayerPosition(PlayerPosition.valueOf(resultSet.getString("position")));
        player.setNationality(resultSet.getString("nationality"));
        player.setNumber(resultSet.getInt("number"));
        player.setAge(resultSet.getInt("age"));
        player.setScored_goals(resultSet.getInt("scored_goals"));
        player.setPlaying_time_seconds(resultSet.getInt("playing_time_seconds"));
        player.setChampionshipName("seria-A");

        return player;
    }
}
