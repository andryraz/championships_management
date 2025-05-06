package com.gestion.fifa.service;

import com.gestion.fifa.dao.operations.ChampionshipCrudOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gestion.fifa.entity.ChampionshipRanking;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChampionshipService {
    private final ChampionshipCrudOperations championshipCrudOperations;

    public List<ChampionshipRanking> getChampionshipRankings() {
        return championshipCrudOperations.findAllOrderedByMedianAsc();
    }
}
