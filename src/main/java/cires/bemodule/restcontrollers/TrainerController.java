package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.PatchTrainerRequest;
import cires.bemodule.dtos.views.TrainerDTO;
import cires.bemodule.dtos.requests.CreateTrainerRequest;
import cires.bemodule.services.TrainerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('trainer:read')")
    public ResponseEntity<TrainerDTO> getTrainerById(@PathVariable Long id){
        TrainerDTO trainer = trainerService.findTrainerById(id);
        return ResponseEntity.ok(trainer);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('trainer:read')")
    public ResponseEntity<Page<TrainerDTO>> getAllTrainers(@RequestParam(required = false) String speciality,
                                                           @PageableDefault(size = 20) Pageable pageable){
        Page<TrainerDTO> trainers = trainerService.findAll(speciality, pageable);
        return ResponseEntity.ok(trainers);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('trainer:create')")
    public ResponseEntity<TrainerDTO> createTrainer(@Valid @RequestBody CreateTrainerRequest request, @PathVariable Long userId){
        TrainerDTO trainer = trainerService.createTrainer(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('trainer:delete')")
    ResponseEntity<Void> deleteTrainer(@PathVariable Long id){
        trainerService.deleteTrainer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('trainer:update')")
    public ResponseEntity<TrainerDTO> patch(@PathVariable Long id,
                                            @RequestBody @Valid PatchTrainerRequest request) {
        return ResponseEntity.ok(trainerService.patchTrainer(id, request));
    }
}