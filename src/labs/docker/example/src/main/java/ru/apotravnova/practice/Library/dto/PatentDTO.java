package ru.apotravnova.practice.Library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import ru.apotravnova.practice.Library.model.Author;

import java.util.Date;

public class PatentDTO {

    private int id;
    @NotEmpty(message = "Title the patent should be filled!")
    private String title;

    private Date dataRegistr;

    private int idAuthor; //вместо Author author

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDataRegistr() {
        return dataRegistr;
    }

    public void setDataRegistr(Date dataRegistr) {
        this.dataRegistr = dataRegistr;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
