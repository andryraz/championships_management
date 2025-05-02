package api.bundesliga.service;


import api.bundesliga.dao.operations.ClubCrudOperations;
import api.bundesliga.dao.operations.PlayerCrudOperations;
import api.bundesliga.entity.Club;
import api.bundesliga.entity.Player;

import api.bundesliga.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubCrudOperations clubCrudOperations;

    public List<Club> getAll(Integer page, Integer size) {
        return clubCrudOperations.getAll(page, size);
    }

    public List<Player> getPlayerByIdClub(String id) {
        return clubCrudOperations.findPlayerByIdClub(id);
    }

    public List<StatClub> getStatForSpecificSeason(Integer seasonYear) {
        return clubCrudOperations.getStatForSpecificSeason(seasonYear);
    }

    public List<Club> saveAll(List<Club> clubs) {
        return clubCrudOperations.saveAll(clubs);
    }

//    public List<Player> saveAll(List<Player> players) {
//        return playerCrudOperations.saveAll(players);
//    }
//

}
