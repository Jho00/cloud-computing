package ru.apotravnova.practice.Library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id_book")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //уникальное значение
    private int id;

    @Column(name = "title_book")
    @NotEmpty(message = "Title the book should br filled ")
    private String title;

    //у книги много авотров - один ко многим
    //@Cascade=SAVE_UPDATE
    //private int idAuthor;
//    @OneToMany
//    private List<Author> authors;

    public Book() {}
}
