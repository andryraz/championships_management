package api.championship_management.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Season {
    private String id;
    private String alias;
    private int year;
    private SeasonStatus status;
}
