package org.example.services;

import org.example.entities.Role;
import org.example.enums.ERole;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Role getByName(ERole eRole);
}
