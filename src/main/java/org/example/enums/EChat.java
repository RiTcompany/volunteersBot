package org.example.enums;

import lombok.Getter;

@Getter
public enum EChat {
    PERSONAL("Личные чаты"),
    EVENT("Участники мероприятия"),
    GROUP("Групповые чаты");

    private final String value;

    EChat(String value) {
        this.value = value;
    }
}
