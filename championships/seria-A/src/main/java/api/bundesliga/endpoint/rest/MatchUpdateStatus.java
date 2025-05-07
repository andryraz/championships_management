package api.bundesliga.endpoint.rest;

import api.bundesliga.entity.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchUpdateStatus {
    private MatchStatus status;
}
