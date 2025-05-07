package com.gestion.fifa.service;

import com.gestion.fifa.entity.StatClub;
import com.gestion.fifa.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ChampionshipClient {

    private final RestTemplate restTemplate;

    @Value("${championship.bundesliga.port}")
    private int bundesligaPort;

    @Value("${championship.seria-A.port}")
    private int seriaAPort;

    @Value("${championship.ligue-1.port}")
    private int ligue1Port;

    @Value("${championship.premier-league.port}")
    private int premierLeaguePort;

    @Value("${championship.la-liga.port}")
    private int laligaPort;

    public List<StatClub> fetchClubStatistics(String championship) {
        int port = resolvePort(championship);
        String url = "http://localhost:" + port + "/clubs/statistics";
        ResponseEntity<StatClub[]> response = restTemplate.getForEntity(url, StatClub[].class);
        return Arrays.asList(response.getBody());
    }

    public List<StatPlayer> fetchPlayerStatistics(String championship) {
        int port = resolvePort(championship);
        String url = "http://localhost:" + port + "/players/statistics";
        ResponseEntity<StatPlayer[]> response = restTemplate.getForEntity(url, StatPlayer[].class);
        return Arrays.asList(response.getBody());
    }

    private int resolvePort(String championship) {
        return switch (championship.toLowerCase()) {
            case "bundesliga" -> bundesligaPort;
            case "premier-league" -> premierLeaguePort;
            case "ligue-1" -> ligue1Port;
            case "seria-A" -> seriaAPort;
            case "la-liga" -> laligaPort;
            default -> throw new IllegalArgumentException("Unknown championship: " + championship);
        };
    }
}
