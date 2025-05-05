package com.gestion.fifa.service;

import com.gestion.fifa.entity.StatClub;
import com.gestion.fifa.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;


@Component

public class ChampionshipClient {
    private final RestTemplate restTemplate;

    public ChampionshipClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<StatClub> fetchClubStatistics() {
        String url = "http://localhost:8081/clubs/statistics";
        ResponseEntity<StatClub[]> response = restTemplate.getForEntity(url, StatClub[].class);
        return Arrays.asList(response.getBody());
    }

    public List<StatPlayer> fetchPlayerStatistics() {
        String url = "http://localhost:8081/players/statistics";
        ResponseEntity<StatPlayer[]> response = restTemplate.getForEntity(url, StatPlayer[].class);
        return Arrays.asList(response.getBody());
    }
}

