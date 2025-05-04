package api.bundesliga.endpoint;

import api.bundesliga.entity.Match;
import api.bundesliga.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @PostMapping("/matchMaker/{seasonYear}")
    public ResponseEntity<List<Match>> generateMatches(@PathVariable int seasonYear) {
        List<Match> matches = matchService.generateMatchsForSeason(seasonYear);
        return ResponseEntity.ok(matches);
    }
}
