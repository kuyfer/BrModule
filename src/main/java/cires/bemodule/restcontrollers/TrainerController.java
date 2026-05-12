package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.TrainerDTO;
import cires.bemodule.dtos2.CreateTrainerRequest;
import cires.bemodule.entities.Trainer;
import cires.bemodule.mappers.TrainerMapper;
import cires.bemodule.services.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    public TrainerController(TrainerService trainerService, TrainerMapper trainerMapper){
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

@GetMapping
public ResponseEntity<List<TrainerDTO>> getAllTrainers(@RequestParam(required = false) String speciality){
    List<TrainerDTO> trainers = trainerService.findAll(speciality);
    return ResponseEntity.ok(trainers);
}

@GetMapping("/{id}")
    public ResponseEntity<TrainerDTO> getTrainerById(@PathVariable Long id){
        TrainerDTO trainer = trainerService.findTrainerById(id);
        return ResponseEntity.ok(trainer);
    }

@PostMapping("/{userId}")
public  ResponseEntity<TrainerDTO> createTrainer(@Valid @RequestBody CreateTrainerRequest request, @PathVariable Long userId){
       TrainerDTO trainer = trainerService.createTrainer(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainer);
}

@DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTrainer(@PathVariable Long id){
        trainerService.deleteTrainer(id);
        return ResponseEntity.noContent().build();
    }
}
