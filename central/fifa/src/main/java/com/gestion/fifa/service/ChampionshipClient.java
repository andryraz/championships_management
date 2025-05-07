package com.gestion.fifa.service;

import com.gestion.fifa.entity.StatClub;
import com.gestion.fifa.entity.StatPlayer;
import com.gestion.fifa.service.exception.ChampionshipSyncException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    @Value("${championship.premier-league.port}")
    private int premierLeaguePort;

    @Value("${championship.ligue-1.port}")
    private int ligue1Port;

    @Value("${championship.seria.port}")
    private int seriaPort;

    @Value("${championship.la-liga.port}")
    private int ligaPort;

    @Value("${championship.api-key}")
    private String apiKey;

    public List<StatClub> fetchClubStatistics(String championship) {
        int port = resolvePort(championship);
        String url = "http://localhost:" + port + "/clubs/statistics";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<StatClub[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    StatClub[].class
            );
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new ChampionshipSyncException("Erreur lors de la récupération des clubs pour " + championship, e);
        }
    }

    public List<StatPlayer> fetchPlayerStatistics(String championship) {
        int port = resolvePort(championship);
        String url = "http://localhost:" + port + "/players/statistics";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<StatPlayer[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    StatPlayer[].class
            );
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            throw new ChampionshipSyncException("Erreur lors de la récupération des joueurs pour " + championship, e);
        }
    }

    private int resolvePort(String championship) {
        return switch (championship.toLowerCase()) {
            case "bundesliga" -> bundesligaPort;
            case "seria" -> seriaPort;
            case "premier-league" -> premierLeaguePort;
            case "ligue-1" -> ligue1Port;
            case "la-liga" -> ligaPort;
            default -> throw new ChampionshipSyncException("Championnat inconnu : " + championship);
        };
    }
}
