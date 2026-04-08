package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.TrainerDTO;
import cires.bemodule.entities.Trainer;
import cires.bemodule.services.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService){
        this.trainerService = trainerService;
    }

@GetMapping
    public ResponseEntity<List<TrainerDTO>> getAllTrainers(){
        List<TrainerDTO> trainers = trainerService.findAll();
        return ResponseEntity.ok(trainers);
}
@GetMapping("/{id}")
    public ResponseEntity<TrainerDTO> getTrainerById(@PathVariable Long id){
        TrainerDTO trainer = trainerService.findById(id);
        return ResponseEntity.ok(trainer);
    }

@PostMapping("/{userId}")
public  ResponseEntity<Void> createTrainer(@RequestBody Trainer trainer, @PathVariable Long userId){
        trainerService.createTrainer(trainer, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
}

@DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTrainer(@PathVariable Long id){
        trainerService.deleteTrainer(id);
        return ResponseEntity.noContent().build();
    }
}
