package cires.bemodule.services;

import cires.bemodule.dtos.TrainerDTO;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.User;
import cires.bemodule.exceptions.validationexceptions.ConflictException;
import cires.bemodule.exceptions.controllerexceptions.TrainerNotFoundException;
import cires.bemodule.exceptions.controllerexceptions.UserNotFoundException;
import cires.bemodule.mappers.TrainerMapper;
import cires.bemodule.repositories.TrainerRepository;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.specifications.TrainerSpecifications;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UserRepository userRepository;

    TrainerService(TrainerRepository trainerRepository, TrainerMapper trainerMapper, UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.trainerMapper = trainerMapper;
        this.userRepository = userRepository;
    }

    // ################################# CREATE ######################################

    public Trainer createTrainer(Trainer trainer, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if(trainerRepository.existsByUserId(user.getId())){
            throw new ConflictException("Trainer already exists for this user");
        }
        Trainer newTrainer = new Trainer();
        newTrainer.setSpeciality(trainer.getSpeciality());
        newTrainer.setUser(user);

        return trainerRepository.save(newTrainer);
    }

    // ################################# READ ########################################

    public TrainerDTO findTrainerById(Long id) {
        return trainerMapper.toTrainerDTO(getTrainerIdOrThrow(id));
    }

     public List<TrainerDTO> findAll(String specialty) {
        Specification<Trainer> spec = Specification
                .where(TrainerSpecifications.hasSpeciality(specialty));
        List<Trainer> trainers = trainerRepository.findAll(spec);
        return trainers.stream()
                .map(trainerMapper::toTrainerDTO)
                .toList();
     }

    // ################################# UPDATE ######################################

    public Trainer updateTrainer(Long id, Trainer trainer) {
        Trainer existingTrainer = getTrainerIdOrThrow(id);
        existingTrainer.setSpeciality(trainer.getSpeciality());
        return trainerRepository.save(existingTrainer);
    }

    // ################################# DELETE ######################################

     public void deleteTrainer(Long id) {
        Trainer trainer = getTrainerIdOrThrow(id);
        trainerRepository.delete(trainer);
     }

     // ################################# UTILS ######################################

    private Trainer getTrainerIdOrThrow(Long id){
        return trainerRepository.findById(id).orElseThrow( () -> new TrainerNotFoundException(id));
    }
}
