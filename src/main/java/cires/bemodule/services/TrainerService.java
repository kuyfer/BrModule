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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // ################################# CREATE ######################################

    public TrainerDTO createTrainer(CreateTrainerRequest request, Long userId) {
        logger.info("Creating trainer for user id: {}, speciality: {}", userId, request.getSpeciality());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", userId);
                    return new UserNotFoundException(userId);
                });

        if (trainerRepository.existsByUserId(user.getId())) {
            logger.warn("Trainer already exists for user id: {}", userId);
            throw new ConflictException("Trainer already exists for this user");
        }

        Trainer trainer = trainerMapper.toTrainer(request);
        trainer.setSpeciality(request.getSpeciality());
        Role trainerRole = roleRepository.findByroleName(RoleType.TRAINER)
                .orElseThrow(() -> {
                    logger.error("Role not found: TRAINER");
                    return new RoleNotFoundException();
                });
        user.setRoles(List.of(trainerRole));
        trainer.setUser(user);
        Trainer savedTrainer = trainerRepository.save(trainer);
        logger.info("Trainer created with id: {} for user id: {}", savedTrainer.getId(), userId);
        return trainerMapper.toTrainerDTO(savedTrainer);
    }

    // ################################# READ ########################################

    public TrainerDTO findTrainerById(Long id) {
        logger.info("Finding trainer by id: {}", id);
        Trainer trainer = getTrainerIdOrThrow(id);
        TrainerDTO dto = trainerMapper.toTrainerDTO(trainer);
        logger.info("Found trainer with id: {}", id);
        return dto;
    }

    public List<TrainerDTO> findAll(String specialty) {
        logger.info("Finding all trainers with specialty filter: {}", specialty);
        Specification<Trainer> spec = Specification
                .where(TrainerSpecifications.hasSpeciality(specialty));
        List<Trainer> trainers = trainerRepository.findAll(spec);
        List<TrainerDTO> dtos = trainers.stream()
                .map(trainerMapper::toTrainerDTO)
                .toList();
        logger.info("Found {} trainers matching specialty: {}", dtos.size(), specialty);
        return dtos;
    }

    // ################################# UPDATE ######################################

    public Trainer updateTrainer(Long id, Trainer trainer) {
        logger.info("Updating trainer with id: {}", id);
        Trainer existingTrainer = getTrainerIdOrThrow(id);
        existingTrainer.setSpeciality(trainer.getSpeciality());
        Trainer updated = trainerRepository.save(existingTrainer);
        logger.info("Trainer updated with id: {}", id);
        return updated;
    }

    // ################################# DELETE ######################################

    public void deleteTrainer(Long id) {
        logger.info("Deleting trainer with id: {}", id);
        Trainer trainer = getTrainerIdOrThrow(id);
        trainerRepository.delete(trainer);
        logger.info("Trainer deleted with id: {}", id);
    }

    // ################################# UTILS ######################################

    private Trainer getTrainerIdOrThrow(Long id) {
        logger.debug("Looking up trainer by id: {}", id);
        return trainerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Trainer not found with id: {}", id);
                    return new TrainerNotFoundException(id);
                });
    }
}