package cires.bemodule.services;

import cires.bemodule.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserService userservice;

    public RoleService(RoleRepository roleRepository, UserService userservice) {
        this.roleRepository = roleRepository;
        this.userservice = userservice;
    }
}
