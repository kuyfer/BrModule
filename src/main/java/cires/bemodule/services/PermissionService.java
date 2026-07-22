package cires.bemodule.services;

import cires.bemodule.dtos.views.PermissionDTO;
import cires.bemodule.mappers.PermissionMapper;
import cires.bemodule.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionDTO> getAllPermissions() {
        log.debug("Fetching all permissions");
        List<PermissionDTO> permissions = permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionDto)
                .toList();
        log.debug("Found {} permissions", permissions.size());
        return permissions;
    }
}