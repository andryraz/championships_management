package api.bundesliga.service;


import api.bundesliga.dao.operations.PlayerCrudOperations;

import api.bundesliga.endpoint.rest.StatPlayerRest;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.StatPlayer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerCrudOperations playerCrudOperations;

    public List<Player> getAll(Integer page, Integer size) {
        return playerCrudOperations.getAll(page, size);
    }

    public List<Player> saveAll(List<Player> players) {
        return playerCrudOperations.saveAll(players);
    }

    public StatPlayer getStatPlayer(String player_id, Integer seasonYear) {
        return playerCrudOperations.findByIdPlayer(player_id, seasonYear);
    }

    public List<StatPlayerRest> getStat() {
        return playerCrudOperations.getStat();
    }
}