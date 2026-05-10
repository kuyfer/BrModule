package cires.bemodule.repositories;

import cires.bemodule.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


    Optional<User> findByEmail(String email);

    User findByUsername(String username);

    long deleteByEmailIgnoreCase(String email);

}
