package ru.apotravnova.practice.Library.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import ru.apotravnova.practice.Library.model.Patent;

import java.util.Date;
import java.util.List;

//описываются поля, которые приходят от клиента
public class AuthorDTO {

    private int id;

    @NotEmpty(message = "Name should be filled!")
    private String name;

    @NotEmpty(message = "Surname should be filled!")
    private String surname;

    private String patronymic;

//    @NotEmpty(message = "Date should be filled!")
    private Date dateOfBirth;
//    private List<Patent> patents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //    public List<Patent> getPatents() {
//        return patents;
//    }
//
//    public void setPatents(List<Patent> patents) {
//        this.patents = patents;
//    }
}
