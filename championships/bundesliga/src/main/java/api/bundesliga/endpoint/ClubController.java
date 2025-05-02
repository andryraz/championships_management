package api.bundesliga.endpoint;

import api.bundesliga.endpoint.rest.PlayerRest;
import api.bundesliga.entity.Club;

import api.bundesliga.entity.Player;


import api.bundesliga.entity.StatClub;
import api.bundesliga.service.ClubService;
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
public class ClubController {
    private final ClubService clubService;

    @GetMapping("/clubs")
    public ResponseEntity<List<Club>> getClubs(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        List<Club> clubs = clubService.getAll(page, pageSize);
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/clubs/{id}/players")
    public ResponseEntity<List<Player>> getPlayerById(
           @PathVariable("id") String id
   ) {
       return ResponseEntity.ok(clubService.getPlayerByIdClub(id));
    }

    @PutMapping("/clubs")
    public ResponseEntity<Object> saveOrUpdate(@RequestBody List<Club> clubs) {
        try {
            return ResponseEntity.ok().body(clubService.saveAll(clubs));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/clubs/statistics/{seasonYear}")
    public ResponseEntity<List<StatClub>> getStatClub(
            @PathVariable("seasonYear") Integer seasonYear) {
        List<StatClub> statclubs = clubService.getStatForSpecificSeason(seasonYear);
        return ResponseEntity.ok(statclubs);
    }

    @PutMapping("/clubs/{id}/players")
    public ResponseEntity<List<PlayerRest>> replaceClubPlayers(
            @PathVariable String id,
            @RequestBody List<PlayerRest> players) {
        return ResponseEntity.ok(clubService.replacePlayers(id, players));
    }


//    @PostMapping("/player")
//    public ResponseEntity<Object> saveDish(@RequestBody List<Player> players) {
//        try {
//            return ResponseEntity.ok().body(playerService.saveAll(players));
//        } catch (ClientException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (NotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
//        } catch (ServerException e) {
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }



}
