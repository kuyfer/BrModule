package cires.bemodule.services;

import cires.bemodule.entities.Trainer;
import cires.bemodule.repositories.TrainerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;

    TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }
}
