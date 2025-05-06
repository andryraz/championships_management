package com.gestion.fifa.endpoint;

import com.gestion.fifa.service.ChampionshipService;
import com.gestion.fifa.entity.ChampionshipRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/championshipRankings")
public class ChampionshipController {
    private final ChampionshipService championshipservice;

    @GetMapping
    public ResponseEntity<List<ChampionshipRanking>> getRankings() {
        List<ChampionshipRanking> rankings = championshipservice.getChampionshipRankings();
        return ResponseEntity.ok(rankings);
    }
}

