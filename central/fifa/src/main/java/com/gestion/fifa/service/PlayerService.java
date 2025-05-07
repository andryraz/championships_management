package com.gestion.fifa.service;

import com.gestion.fifa.dao.operations.PlayerCrudOperations;
import com.gestion.fifa.endpoint.rest.DurationUnit;
import com.gestion.fifa.endpoint.rest.PlayerRanking;
import com.gestion.fifa.endpoint.rest.PlayingTime;
import com.gestion.fifa.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerCrudOperations repo;


    public List<PlayerRanking> getBestPlayers(int top, DurationUnit unit) {

        List<StatPlayer> all = repo.findAll();
        List<StatPlayer> sorted = all.stream()
                .sorted(Comparator
                        .comparingInt(StatPlayer::getScored_goals).reversed()
                        .thenComparingLong(StatPlayer::getPlaying_time_seconds).reversed()
                )
                .limit(top)
                .collect(Collectors.toList());

        return sorted.stream()
                .map(p -> {
                    long raw = p.getPlaying_time_seconds();
                    long converted;
                    switch (unit) {
                        case MINUTE: converted = raw / 60; break;
                        case HOUR:   converted = raw / 3600; break;
                        default:     converted = raw;
                    }
                    int idx = sorted.indexOf(p) + 1;
                    return PlayerRanking.builder()
                            .rank(idx)
                            .id(p.getId())
                            .name(p.getName())
                            .number(p.getNumber() == null ? 0 : p.getNumber())
                            .position(p.getPlayerPosition().name())
                            .nationality(p.getNationality())
                            .age(p.getAge())
                            .championship(p.getChampionshipName())
                            .scoredGoals(p.getScored_goals())
                            .playingTime(new PlayingTime(converted, unit))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
