package api.bundesliga.endpoint;

import api.bundesliga.endpoint.rest.GoalInput;
import api.bundesliga.entity.Match;
import api.bundesliga.service.ClubService;
import api.bundesliga.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GoalController {
    private final MatchService matchService;

    @PostMapping("/matches/{id}/goals")
    public ResponseEntity<Match> addGoalsToMatch(
            @PathVariable String id,
            @RequestBody List<GoalInput> goals
    ) {
        Match match = matchService.addGoals(UUID.fromString(id), goals);
        return ResponseEntity.ok(match);
    }


}
