package ru.apotravnova.practice.Library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
@Table(name = "Patent")
public class Patent {

    @Id
    @Column(name = "id") //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //уникальное значение
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title the patent should be filled!")
    private String title;

    @Column(name = "date_registr")
    private Date dataRegistr;

    @ManyToOne
    @JoinColumn(name = "id_author", referencedColumnName = "id")
    @JsonIgnoreProperties(value = {"patents"}) //чтобы не было бесконечности + чтобы отображались ссылки (идущие по FK)
    private Author author; //FK

    public Patent() {}

    public Patent(int id, String title, Date dataRegistr, Author author) {
        this.id = id;
        this.title = title;
        this.dataRegistr = dataRegistr;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getDataRegistr() {
        return dataRegistr;
    }

    public void setDataRegistr(Date dataRegistr) {
        this.dataRegistr = dataRegistr;
    }


}
