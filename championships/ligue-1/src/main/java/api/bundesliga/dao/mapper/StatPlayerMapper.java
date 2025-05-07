package api.bundesliga.dao.mapper;

import api.bundesliga.entity.DurationUnit;
import api.bundesliga.entity.PlayingTime;
import api.bundesliga.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@Component
@RequiredArgsConstructor
public class StatPlayerMapper implements Function<ResultSet, StatPlayer> {

    @SneakyThrows
    @Override
    public StatPlayer apply(ResultSet resultSet) {
        int scoredGoals = resultSet.getInt("scored_goals");
        int playingTimeInSeconds = resultSet.getInt("playing_time_seconds");

        PlayingTime pt = PlayingTime.builder()
                .value((double) playingTimeInSeconds)
                .durationUnit(DurationUnit.SECOND)
                .build();

        return StatPlayer.builder()
                .scored_goals(scoredGoals)
                .playingTime(List.of(pt))
                .build();
    }
}
