package com.vehicle.manager.user.service;

import com.vehicle.manager.user.dao.Role;
import com.vehicle.manager.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleByName(String name){
        return roleRepository.getByName(name);
    }

    public Role getReferenceById(UUID roleId) {
        return roleRepository.getReferenceById(roleId);
    }
}
