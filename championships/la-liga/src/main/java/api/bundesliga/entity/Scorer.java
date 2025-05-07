package api.bundesliga.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Scorer {
    private PlayerMin player;
    private Integer minuteOfGoal;
    private Boolean ownGoal;
}
