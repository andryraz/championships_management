package api.bundesliga.endpoint;


import api.bundesliga.dao.operations.TransfertCrudOperations;
import api.bundesliga.entity.Transfert;
import api.bundesliga.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransfertController {
    private final TransfertCrudOperations transfertCrudOperations;

    @GetMapping("/Transfert")
    public ResponseEntity<List<Transfert>> getAllMovements() {
        return ResponseEntity.ok(transfertCrudOperations.findAll());
    }

}
