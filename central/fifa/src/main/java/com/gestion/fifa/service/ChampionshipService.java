package com.gestion.fifa.service;

import com.gestion.fifa.dao.operations.ChampionshipCrudOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gestion.fifa.entity.ChampionshipRanking;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChampionshipService {
    private final ChampionshipCrudOperations championshipCrudOperations;

    public List<ChampionshipRanking> getRankedChampionships() {
        List<ChampionshipRanking> rankings = championshipCrudOperations.findAllRankings();

        rankings.sort(Comparator.comparingDouble(ChampionshipRanking::getDifferenceGoalsMedian));

        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        return rankings;
    }
}
