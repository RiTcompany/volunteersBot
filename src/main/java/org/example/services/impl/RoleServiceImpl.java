package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Role;
import org.example.enums.ERole;
import org.example.repositories.RoleRepository;
import org.example.services.RoleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getByName(ERole eRole) {
        return roleRepository.findByRoleName(eRole)
                .orElseGet(() -> roleRepository.saveAndFlush(new Role(eRole)));

    }
}
