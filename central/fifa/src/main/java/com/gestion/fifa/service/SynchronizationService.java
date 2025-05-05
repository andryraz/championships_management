package com.gestion.fifa.service;

import com.gestion.fifa.dao.operations.StatCrudOperations;
import com.gestion.fifa.entity.StatClub;
import com.gestion.fifa.entity.StatPlayer;
import com.gestion.fifa.entity.SynchronizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SynchronizationService {

    private final StatCrudOperations statCrudOperations;
    private final ChampionshipClient championshipClient;

    public void synchronizeData(SynchronizationRequest request) {
//        Integer seasonYear = request.getSeasonYear();

        // 1. Appels aux API des championnats
        List<StatClub> clubStats = championshipClient.fetchClubStatistics();
        List<StatPlayer> playerStats = championshipClient.fetchPlayerStatistics();

        // 2. Suppression des anciennes données
        statCrudOperations.deleteClubStats();
        statCrudOperations.deletePlayerStats();

        // 3. Insertion des nouvelles données
        statCrudOperations.insertClubStats(clubStats);
        statCrudOperations.insertPlayerStats(playerStats);
    }
}
