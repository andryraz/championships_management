package api.bundesliga.endpoint;

import api.bundesliga.endpoint.rest.GoalInput;
import api.bundesliga.entity.Match;
import api.bundesliga.entity.MatchStatus;
import api.bundesliga.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @PostMapping("/matchMaker/{seasonYear}")
    public ResponseEntity<List<Match>> generateMatches(@PathVariable int seasonYear) {
        List<Match> matches = matchService.generateMatchsForSeason(seasonYear);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/matches/{seasonYear}")
    public List<Match> getMatchesBySeason(
            @PathVariable int seasonYear,
            @RequestParam(required = false) MatchStatus matchStatus,
            @RequestParam(required = false) String clubPlayingName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate matchAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate matchBeforeOrEquals
    ) {
        return matchService.findBySeason(
                seasonYear,
                matchStatus,
                clubPlayingName,
                matchAfter,
                matchBeforeOrEquals
        );
    }

    @PostMapping("/matches/{id}/goals")
    public ResponseEntity<Match> addGoalsToMatch(
            @PathVariable String id,
            @RequestBody List<GoalInput> goals
    ) {
        Match match = matchService.addGoals(UUID.fromString(id), goals);
        return ResponseEntity.ok(match);
    }
}
