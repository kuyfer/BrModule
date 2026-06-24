package cires.bemodule.services;

import cires.bemodule.dtos.views.TrainerDTO;
import cires.bemodule.dtos.requests.CreateTrainerRequest;
import cires.bemodule.entities.Role;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.User;
import cires.bemodule.enums.RoleType;
import cires.bemodule.exceptions.controllerexceptions.RoleNotFoundException;
import cires.bemodule.exceptions.validationexceptions.ConflictException;
import cires.bemodule.exceptions.controllerexceptions.TrainerNotFoundException;
import cires.bemodule.exceptions.controllerexceptions.UserNotFoundException;
import cires.bemodule.mappers.TrainerMapper;
import cires.bemodule.repositories.RoleRepository;
import cires.bemodule.repositories.TrainerRepository;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.specifications.TrainerSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // ################################# CREATE ######################################

    public TrainerDTO createTrainer(CreateTrainerRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (trainerRepository.existsByUserId(user.getId())) {
            throw new ConflictException("Trainer already exists for this user");
        }

        Trainer trainer = trainerMapper.toTrainer(request);
        trainer.setSpeciality(request.getSpeciality());
        Role trainerRole = roleRepository.findByroleName(RoleType.TRAINER)
                .orElseThrow(() -> new RoleNotFoundException());
        user.setRoles(List.of(trainerRole));
        trainer.setUser(user);
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.toTrainerDTO(savedTrainer);
    }

    // ################################# READ ########################################

    public TrainerDTO findTrainerById(Long id) {
        Trainer trainer = getTrainerIdOrThrow(id);
        return trainerMapper.toTrainerDTO(trainer);
    }

    public Page<TrainerDTO> findAll(String specialty, Pageable pageable) {
        Specification<Trainer> spec = Specification
                .where(TrainerSpecifications.hasSpeciality(specialty));
        Page<Trainer> trainerPage = trainerRepository.findAll(spec, pageable);
        return trainerPage.map(trainerMapper::toTrainerDTO);
    }

    public List<TrainerDTO> findAll(String specialty) {
        Page<TrainerDTO> page = findAll(specialty, Pageable.unpaged());
        return page.getContent();
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

    private Trainer getTrainerIdOrThrow(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException(id));
    }
}