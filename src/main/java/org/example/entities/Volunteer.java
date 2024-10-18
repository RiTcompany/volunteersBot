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
import org.example.enums.EAnorak;
import org.example.enums.EClothingSize;
import org.example.enums.EColor;
import org.example.enums.EEducationStatus;
import org.example.enums.EGender;

import java.util.Date;

@Entity
@Table(name = "volunteer")
@Getter
@Setter
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private Date birthday;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String phone;

    @Column(name = "tg_link", nullable = false)
    private String tgLink;

    @Column(name = "education_status")
    @Enumerated(EnumType.STRING)
    private EEducationStatus educationStatus;

    @Column(name = "education_institution")
    private String educationInstitution;

    @Column(name = "educational_specialty")
    private String educationalSpecialty;

    @Column(name = "spb_district")
    private String spbDistrict;

    private String vk;

    @Column(name = "clothing_size")
    @Enumerated(EnumType.STRING)
    private EClothingSize clothingSize;

    @Column(name = "has_anorak")
    private Boolean hasAnorak;

    @Column(name = "anorak_type")
    @Enumerated(EnumType.STRING)
    private EAnorak anorakType;

    @Column(name = "has_sweatshirt")
    private Boolean hasSweatshirt;

    @Column(name = "has_t_shirt")
    private Boolean hasTShirt;

    private String reason;

    private String experience;

    private String email;

    @Column(name = "volunteer_id")
    private Long volunteerId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

//    =======================================================

    @Enumerated(EnumType.STRING)
    private EColor color;

    private Double rank;

    private boolean hasInterview;

    private boolean testing;
}