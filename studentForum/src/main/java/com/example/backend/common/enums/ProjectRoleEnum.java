package com.example.backend.common.enums;

public enum ProjectRoleEnum {
    owner(0),
    developer(1)
    ;
    private final int projectRole;

    ProjectRoleEnum(int projectRole) {
        this.projectRole = projectRole;
    }

    public int getProjectRole() {
        return projectRole;
    }
}
