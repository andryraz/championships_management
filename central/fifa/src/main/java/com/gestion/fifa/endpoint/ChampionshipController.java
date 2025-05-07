package com.gestion.fifa.endpoint;

import com.gestion.fifa.service.ChampionshipService;
import com.gestion.fifa.entity.ChampionshipRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChampionshipController {
    private final ChampionshipService championshipservice;

    @GetMapping("/championshipRankings")
    public ResponseEntity<List<ChampionshipRanking>> getRankings() {
        try {
            List<ChampionshipRanking> rankings = championshipservice.getRankedChampionships();
            return ResponseEntity.ok(rankings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

