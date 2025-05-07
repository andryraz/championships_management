package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Goal {
    private UUID id;
    private UUID matchId;
    private UUID scorerId;
    private UUID clubId;
    private int minuteOfGoal;
    private boolean ownGoal;
}
