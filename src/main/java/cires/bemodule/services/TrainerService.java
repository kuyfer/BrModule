package cires.bemodule.services;

import cires.bemodule.dtos.requests.PatchTrainerRequest;
import cires.bemodule.dtos.views.TrainerDTO;
import cires.bemodule.dtos.requests.CreateTrainerRequest;
import cires.bemodule.entities.Role;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.User;
import cires.bemodule.exceptions.notfound.RoleNotFoundException;
import cires.bemodule.exceptions.business.ConflictException;
import cires.bemodule.exceptions.notfound.TrainerNotFoundException;
import cires.bemodule.exceptions.notfound.UserNotFoundException;
import cires.bemodule.mappers.TrainerMapper;
import cires.bemodule.repositories.RoleRepository;
import cires.bemodule.repositories.TrainerRepository;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.specifications.TrainerSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // ################################# CREATE ######################################

    public TrainerDTO createTrainer(CreateTrainerRequest request, Long userId) {
        log.info("Creating trainer for user id: {}, speciality: {}", userId, request.getSpeciality());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", userId);
                    return new UserNotFoundException(userId);
                });

        if (trainerRepository.existsByUserId(user.getId())) {
            log.warn("Trainer already exists for user id: {}", userId);
            throw new ConflictException("Trainer already exists for this user");
        }

        Trainer trainer = trainerMapper.toTrainer(request);
        trainer.setSpeciality(request.getSpeciality());
        Role trainerRole = roleRepository.findByRoleName("TRAINER")
                .orElseThrow(() -> {
                    log.error("Trainer role not found");
                    return new RoleNotFoundException();
                });

        user.getRoles().add(trainerRole);
        userRepository.save(user);

        trainer.setUser(user);
        Trainer savedTrainer = trainerRepository.save(trainer);
        log.info("Trainer created successfully with id: {}", savedTrainer.getId());
        return trainerMapper.toTrainerDTO(savedTrainer);
    }

    // ################################# READ ########################################

    public TrainerDTO findTrainerById(Long id) {
        log.debug("Finding trainer by id: {}", id);
        Trainer trainer = getTrainerIdOrThrow(id);
        return trainerMapper.toTrainerDTO(trainer);
    }

    public Page<TrainerDTO> findAll(java.lang.String specialty, Pageable pageable) {
        log.debug("Fetching trainers page - speciality: {}, pageable: {}", specialty, pageable);
        Specification<Trainer> spec = Specification
                .where(TrainerSpecifications.hasSpeciality(specialty));
        Page<Trainer> trainerPage = trainerRepository.findAll(spec, pageable);
        log.debug("Found {} trainers (page {} of {})", trainerPage.getNumberOfElements(), trainerPage.getNumber(), trainerPage.getTotalPages());
        return trainerPage.map(trainerMapper::toTrainerDTO);
    }

    public List<TrainerDTO> findAll(java.lang.String specialty) {
        log.debug("Fetching all trainers (unpaged) - speciality: {}", specialty);
        Page<TrainerDTO> page = findAll(specialty, Pageable.unpaged());
        return page.getContent();
    }

    // ################################# UPDATE ######################################

    public TrainerDTO patchTrainer(Long id, PatchTrainerRequest request) {
        log.info("Patching Trainer id={} with request: {}", id, request);
        Trainer trainer = getTrainerIdOrThrow(id);
        trainerMapper.patchTrainerFromRequest(request, trainer);
        Trainer saved = trainerRepository.save(trainer);
        log.info("Trainer patched id={}, speciality={}", saved.getId(), saved.getSpeciality());
        return trainerMapper.toTrainerDTO(saved);
    }

    // ################################# DELETE ######################################

    public void deleteTrainer(Long id) {
        log.info("Deleting trainer with id: {}", id);
        Trainer trainer = getTrainerIdOrThrow(id);
        trainerRepository.delete(trainer);
        log.info("Trainer deleted successfully with id: {}", id);
    }

    // ################################# UTILS ######################################

    private Trainer getTrainerIdOrThrow(Long id) {
        log.debug("Looking up trainer by id: {}", id);
        return trainerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Trainer not found with id: {}", id);
                    return new TrainerNotFoundException(id);
                });
    }
}