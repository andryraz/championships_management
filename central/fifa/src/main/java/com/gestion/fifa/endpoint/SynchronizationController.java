package com.gestion.fifa.endpoint;

import com.gestion.fifa.entity.SynchronizationRequest;
import com.gestion.fifa.service.SynchronizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/synchronization")
@RequiredArgsConstructor
public class SynchronizationController {

    private final SynchronizationService synchronizationService;

    @PostMapping
    public ResponseEntity<Void> synchronize() {
            synchronizationService.synchronizeData();
        return ResponseEntity.ok().build();
    }
}
