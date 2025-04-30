package api.championship_management.endpoint;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import api.championship_management.model.player;
import api.championship_management.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlayerController {
    private PlayerService playerService;

    @GetMapping("/player")
    public ResponseEntity<List<player>> getPlayers(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        List<player> players = playerService.getAll(page, pageSize);
        return ResponseEntity.ok(players);
    }


}
