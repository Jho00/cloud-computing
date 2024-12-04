package ru.apotravnova.practice.Library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PublishingOffice")
public class PublishingOffice {

    @Id
    @Column(name = "id_publishing_office")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public PublishingOffice() {}
}
