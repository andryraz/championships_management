package api.bundesliga.endpoint;

import api.bundesliga.endpoint.rest.UpdateSeasonStatus;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.Season;
import api.bundesliga.service.SeasonService;
import api.bundesliga.service.exception.ClientException;
import api.bundesliga.service.exception.NotFoundException;
import api.bundesliga.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class SeasonController {
    private final SeasonService seasonService;

    @GetMapping("/seasons")
    public ResponseEntity<List<Season>> getSeason(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        List<Season> seasons = seasonService.getAll(page, pageSize);
        return ResponseEntity.ok(seasons);
    }

    @PostMapping("/seasons")
    public ResponseEntity<Object> saveSeason(@RequestBody List<Season> seasons) {
        try {
            return ResponseEntity.ok().body(seasonService.saveAll(seasons));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PutMapping("/seasons/{seasonYear}/status")
    public ResponseEntity<Object> updateSeasonStatus(
            @PathVariable int seasonYear,
            @RequestBody UpdateSeasonStatus request) {

        try {
            return ResponseEntity.ok(seasonService.updateStatus(seasonYear, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
