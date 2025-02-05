package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    private String location;

    @Column(name = "participants_link")
    private String participantsLink;

    @Column(name = "team_leader")
    private String teamLeader;

    @Column(name = "center_id")
    private Long centerId;

    @Column(name = "headquarters_id")
    private Long headquartersId;

    @Column(name = "group_chat_link")
    private String groupChatLink;

    @Column(name = "setting_participant_link")
    private String settingParticipantLink;

    @Column(name = "answerable_id")
    private Long answerableId;

    @Column(name = "registration_link")
    private String registrationLink;

    @Column(name = "federal_id")
    private Long federalId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "training_link")
    private String trainingLink;

    @Column(name = "results_link")
    private String resultsLink;

    @Column(name = "is_available_for_registration")
    private Boolean isAvailable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}