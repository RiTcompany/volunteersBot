package org.example.enums;

import lombok.Getter;

@Getter
public enum ERole {
    ROLE_REGIONAL_HEAD(7),
    ROLE_REGIONAL_TEAM_HEAD(6),
    ROLE_HEADQUARTER_CURATOR(5),
    ROLE_DISTRICT_CURATOR(14),
    ROLE_UNIVERSITY_COMMUNITY_CENTER_CURATOR(5),
    ROLE_SCHOOL_SQUAD_CURATOR(5),
    ROLE_TEAM_LEADER(1),

    ROLE_BOSS(-1),
    ROLE_MODERATOR(-2),
    ROLE_WRITER(-2);

    private final int roleRank;

    ERole(int roleRank) {
        this.roleRank = roleRank;
    }

    public static int compare(ERole eRole1, ERole eRole2) {
        return roleFromOneGroup(eRole1, eRole2) && eRole1.getRoleRank() > eRole2.getRoleRank() ? 1 : -1;
    }

    private static boolean roleFromOneGroup(ERole eRole1, ERole eRole2) {
        return eRole1.getRoleRank() * eRole2.getRoleRank() > 0;
    }
}
