package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "volunteer_event")
public class VolunteerEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "volunteer_id")
    private Long volunteerId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "is_here", columnDefinition = "boolean default 'false'")
    private Boolean isHere;
}
