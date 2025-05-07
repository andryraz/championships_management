package api.bundesliga.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Season {
    @JsonIgnore
    private String id;
    private String alias;
    private int year;
    private SeasonStatus status;
}