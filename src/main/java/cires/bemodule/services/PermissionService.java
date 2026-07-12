package cires.bemodule.services;

import cires.bemodule.dtos.views.PermissionDTO;
import cires.bemodule.mappers.PermissionMapper;
import cires.bemodule.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionDto)
                .toList();
    }
}