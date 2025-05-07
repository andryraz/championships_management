package api.bundesliga.service;

import api.bundesliga.dao.operations.PlayerCrudOperations;
import api.bundesliga.dao.operations.SeasonCrudOperations;
import api.bundesliga.endpoint.rest.UpdateSeasonStatus;
import api.bundesliga.entity.Player;
import api.bundesliga.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonCrudOperations seasonCrudOperations;

    public List<Season> getAll(Integer page, Integer size) {
        return seasonCrudOperations.getAll(page, size);
    }

    public List<Season> saveAll(List<Season> seasons) {
        return seasonCrudOperations.saveAll(seasons);
    }

    public Season updateStatus(int year, UpdateSeasonStatus updateStatus) {
        return seasonCrudOperations.updateSeasonStatus(year, updateStatus.getStatus());
    }

}
