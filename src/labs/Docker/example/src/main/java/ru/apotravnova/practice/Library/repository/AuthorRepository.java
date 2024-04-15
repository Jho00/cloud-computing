package ru.apotravnova.practice.Library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.apotravnova.practice.Library.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
