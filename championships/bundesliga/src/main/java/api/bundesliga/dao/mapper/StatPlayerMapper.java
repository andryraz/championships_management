package api.bundesliga.dao.mapper;

import api.bundesliga.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class StatPlayerMapper implements Function<ResultSet, StatPlayer> {

    @SneakyThrows
    @Override
    public StatPlayer apply(ResultSet resultSet) {

        StatPlayer player = new StatPlayer();
        player.setScored_goals(resultSet.getInt("scored_goals"));
        player.setPlaying_time_seconds(resultSet.getInt("playing_time_seconds"));

        return player;
    }
}
