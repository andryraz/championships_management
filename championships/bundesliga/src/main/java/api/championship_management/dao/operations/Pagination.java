package api.championship_management.dao.operations;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pagination {
    private int page;
    private int pageSize;

}