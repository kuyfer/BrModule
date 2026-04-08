package cires.bemodule.services;

import cires.bemodule.dtos.TrainerDTO;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.User;
import cires.bemodule.exceptions.ConflictException;
import cires.bemodule.exceptions.TrainerNotFoundException;
import cires.bemodule.exceptions.UserNotFoundException;
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

    @Transactional(readOnly = true)
     public List<TrainerDTO> findAll() {

        return trainerRepository.findAll()
                .stream()
                .map(trainerMapper::toTrainerDTO)
                .collect(Collectors.toList());
     }
     @Transactional(readOnly = true)
     public TrainerDTO findById(Long id) {
        return trainerMapper.toTrainerDTO(trainerRepository.findById(id).orElseThrow(() -> new TrainerNotFoundException( id)));
     }

     public void deleteTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id).orElseThrow(() -> new TrainerNotFoundException(id));
        trainerRepository.delete(trainer);
     }
}
