package ru.apotravnova.practice.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.apotravnova.practice.Library.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
