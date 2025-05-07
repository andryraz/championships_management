package com.gestion.fifa.service;

import com.gestion.fifa.dao.operations.StatCrudOperations;
import com.gestion.fifa.entity.StatClub;
import com.gestion.fifa.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor

public class SynchronizationService {

    private final StatCrudOperations statCrudOperations;
    private final ChampionshipClient championshipClient;


    public void synchronizeData() {
        List<String> championships = List.of("bundesliga", "premier-league", "ligue-1", "seria", "la-liga");

        statCrudOperations.deleteClubStats();
        statCrudOperations.deletePlayerStats();

        for (String championship : championships) {
            try {
                List<StatClub> clubStats = championshipClient.fetchClubStatistics(championship);
                List<StatPlayer> playerStats = championshipClient.fetchPlayerStatistics(championship);

                statCrudOperations.insertClubStats(clubStats);
                statCrudOperations.insertPlayerStats(playerStats);


                List<Integer> differences = statCrudOperations.findGoalDifferencesByChampionship(championship);

                double median = calculateMedian(differences);

                statCrudOperations.insertChampionshipMedian(championship.toUpperCase(), median);

                System.out.println("Synchronization and median calculation successful for: " + championship);

            } catch (Exception e) {
                System.err.println("Failed to synchronize or compute for: " + championship);
                e.printStackTrace();
            }
        }
    }

    private double calculateMedian(List<Integer> values) {
        if (values == null || values.isEmpty()) return 0;
        Collections.sort(values);
        int n = values.size();
        if (n % 2 == 1) {
            return values.get(n / 2);
        } else {
            return (values.get(n / 2 - 1) + values.get(n / 2)) / 2.0;
        }
    }
}

