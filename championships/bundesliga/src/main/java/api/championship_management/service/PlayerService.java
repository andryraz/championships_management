package api.championship_management.service;


import api.championship_management.dao.operations.PlayerCrudOperations;
import api.championship_management.model.player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private PlayerCrudOperations playerCrudOperations;

    public List<player> getAll(Integer page, Integer size) {
        return playerCrudOperations.getAll(page, size);
    }
}
