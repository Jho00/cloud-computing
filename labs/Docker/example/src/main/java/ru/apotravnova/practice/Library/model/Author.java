package ru.apotravnova.practice.Library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Author")
@DynamicUpdate
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //уникальное значение
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should be filled!")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname should be filled!")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "date_birth")
    private Date dateOfBirth;

    /*
    orphanRemoval = true, чтобы обеспечить правильное удаление потерянных сущностей.
    Если задано это свойство, JPA автоматически удаляет из базы данных все потерянные объекты
     */
    @OneToMany(mappedBy = "author",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = {"author"}) //указывать обязательно, чтобы ссылки (на FK отображало)
    private List<Patent> patents;

    public Author() {}

    public Author(int id, String name, String surname, String patronymic, Date dateOfBirth, List<Patent> patents) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.patents = patents;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Patent> getPatents() {
        return patents;
    }

    public void setPatents(List<Patent> patents) {
        this.patents = patents;
    }
}
