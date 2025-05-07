package api.bundesliga.endpoint;


import api.bundesliga.endpoint.rest.StatPlayerRest;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.StatPlayer;
import api.bundesliga.service.PlayerService;
import api.bundesliga.service.exception.ClientException;
import api.bundesliga.service.exception.NotFoundException;
import api.bundesliga.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        List<Player> players = playerService.getAll(page, pageSize);
        return ResponseEntity.ok(players);
    }

    @PutMapping("/player")
    public ResponseEntity<Object> savePlayers(@RequestBody List<Player> players) {
        try {
            return ResponseEntity.ok().body(playerService.saveAll(players));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/players/{id}/statistics/{seasonYear}")
    public ResponseEntity<StatPlayer> getStatPlayer(
            @PathVariable("id") String player_id,
            @PathVariable("seasonYear") Integer seasonYear
    ) {
        return ResponseEntity.ok(playerService.getStatPlayer(player_id, seasonYear));
    }

    @GetMapping("/players/statistics")
    public ResponseEntity<List<StatPlayerRest>> getStatPlayer() {
        return ResponseEntity.ok(playerService.getStat());
    }

}