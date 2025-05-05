package com.gestion.fifa.service;


import com.gestion.fifa.dao.operations.PlayerCrudOperations;
import com.gestion.fifa.entity.Player;
import com.gestion.fifa.entity.PlayerRanking;
import com.gestion.fifa.util.DurationUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerCrudOperations repository;

    public List<PlayerRanking> getBestPlayers(int top, DurationUnit unit) {
        List<Player> all = repository.findAllPlayers();

        return all.stream()
                .sorted(Comparator.comparingInt(Player::getScoredGoals).reversed()
                        .thenComparingLong(Player::getTotalPlayingTimeSeconds).reversed())
                .limit(top)
                .map(player -> new PlayerRanking(
                        player.getName(),
                        player.getScoredGoals(),
                        unit.convert(player.getTotalPlayingTimeSeconds()),
                        player.getChampionshipName()
                ))
                .collect(Collectors.toList());
    }
}