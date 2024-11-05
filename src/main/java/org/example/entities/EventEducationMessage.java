package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.EMessage;

@Entity
@Getter
@Setter
@Table(name = "event_education_message")
public class EventEducationMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "message_number")
    private int messageNumber;

    @Column(length = 4096)
    private String data;

    @Column(name = "e_message")
    @Enumerated(EnumType.STRING)
    private EMessage eMessage;
}
