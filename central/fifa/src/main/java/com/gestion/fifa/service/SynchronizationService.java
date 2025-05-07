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
        List<String> championships = List.of("bundesliga", "premier-league", "ligue-1", "seria-A", "la-liga");

        statCrudOperations.deleteClubStats();
        statCrudOperations.deletePlayerStats();

        for (String championship : championships) {
            try {
                List<StatClub> clubStats = championshipClient.fetchClubStatistics(championship);
                List<StatPlayer> playerStats = championshipClient.fetchPlayerStatistics(championship);

                statCrudOperations.insertClubStats(clubStats);
                statCrudOperations.insertPlayerStats(playerStats);

                System.out.println("Synchronization successful for championship: " + championship);
            } catch (Exception e) {
                System.err.println("Failed to synchronize data for championship: " + championship);
                e.printStackTrace(); // Ou utilise un logger
            }
        }
    }
}
