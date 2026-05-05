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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
        newTrainer.setSpecialty(trainer.getSpecialty());
        newTrainer.setUser(user);

        return trainerRepository.save(newTrainer);
    }

    // ################################# READ ########################################

    @Transactional(readOnly = true)
    public TrainerDTO findTrainerById(Long id) {
        return trainerMapper.toTrainerDTO(getTrainerIdOrThrow(id));
    }

    // TODO : maybe Transactional(readOnly = true) not needed
    @Transactional(readOnly = true)
     public List<TrainerDTO> findAllTrainers() {

        return trainerRepository.findAll()
                .stream()
                .map(trainerMapper::toTrainerDTO)
                .toList();
     }

    // ################################# UPDATE ######################################

    public Trainer updateTrainer(Long id, Trainer trainer) {
        Trainer existingTrainer = getTrainerIdOrThrow(id);
        existingTrainer.setSpecialty(trainer.getSpecialty());
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
