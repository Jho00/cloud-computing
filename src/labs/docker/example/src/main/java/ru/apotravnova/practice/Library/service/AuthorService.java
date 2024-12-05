package ru.apotravnova.practice.Library.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.apotravnova.practice.Library.dto.AuthorDTO;
import ru.apotravnova.practice.Library.model.Author;
import ru.apotravnova.practice.Library.repository.AuthorRepository;
import ru.apotravnova.practice.Library.util.AuthorNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) //уровень транзакции - только чтение
public class AuthorService {

    //внедрение репозитория
    final private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    //сохранение в бд
    @Transactional
    public void save(Author author) {
        authorRepository.save(author);
    }

    //удаление в бд по переданному id
    @Transactional
    public void delete(int id) {
        getById(id); //получаю автора, если он есть, то удаляю, если нет выкидывается исключение
        authorRepository.deleteById(id);
    }

    //модификация бд
    @Transactional
    public void update(Author updateAuthor) {
        //получаю автора, если он есть, то изменяю, если нет выкидывается исключение
        getById(updateAuthor.getId());
        //т.к. мы указали setId, то save понимает, что будет update
        authorRepository.save(updateAuthor);
    }

    //чтение из бд
    //метод возвращает всех авторов
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    //метод возаращет автора по id
    public Author getById(int id) {
        Optional<Author> authors = authorRepository.findById(id);
        //если автор есть в Optional, то возвращаем его, иначе ошибка
        return authors.orElseThrow(AuthorNotFoundException::new);
    }

}
