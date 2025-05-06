package com.gestion.fifa.endpoint;

import com.gestion.fifa.dto.DurationUnit;
import com.gestion.fifa.dto.PlayerRanking;
import com.gestion.fifa.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {
    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @GetMapping("/bestPlayers")
    public List<PlayerRanking> bestPlayers(
            @RequestParam(defaultValue = "5") int top,
            @RequestParam DurationUnit playingTimeUnit
    ) {
        return service.getBestPlayers(top, playingTimeUnit);
    }
}
