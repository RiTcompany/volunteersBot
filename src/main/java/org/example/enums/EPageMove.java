package org.example.enums;

import lombok.Getter;

@Getter
public enum EPageMove {
    PREV("◀"),
    NEXT("▶");

    private final String value;

    EPageMove(String value) {
        this.value = value;
    }

}
