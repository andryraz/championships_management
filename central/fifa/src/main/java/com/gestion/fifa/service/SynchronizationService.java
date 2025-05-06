package com.gestion.fifa.service;

import com.gestion.fifa.dao.operations.StatCrudOperations;
import com.gestion.fifa.entity.StatClub;
import com.gestion.fifa.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SynchronizationService {

    private final StatCrudOperations statCrudOperations;
    private final ChampionshipClient championshipClient;

    public void synchronizeData() {
//        Integer seasonYear = request.getSeasonYear();

        List<StatClub> clubStats = championshipClient.fetchClubStatistics();
        List<StatPlayer> playerStats = championshipClient.fetchPlayerStatistics();

        statCrudOperations.deleteClubStats();
        statCrudOperations.deletePlayerStats();

        statCrudOperations.insertClubStats(clubStats);
        statCrudOperations.insertPlayerStats(playerStats);
    }
}
