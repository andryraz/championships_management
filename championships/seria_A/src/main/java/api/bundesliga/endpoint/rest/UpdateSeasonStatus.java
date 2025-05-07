package api.bundesliga.endpoint.rest;

import api.bundesliga.entity.SeasonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSeasonStatus {
    private SeasonStatus status;
}