package ru.apotravnova.practice.Library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.apotravnova.practice.Library.model.Author;
import ru.apotravnova.practice.Library.model.Patent;
import ru.apotravnova.practice.Library.repository.PatentRepository;
import ru.apotravnova.practice.Library.util.PatnetNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) //уровень транзакции - только чтение
public class PatentService {
    //внедрение репозитория
    final private PatentRepository patentRepository;
    private final AuthorService authorService;//убрать

    @Autowired
    public PatentService(PatentRepository patentRepository, AuthorService authorService) {
        this.patentRepository = patentRepository;
        this.authorService = authorService; //убрать
    }

    //сохранение в бд
    @Transactional
    public void save(Patent patent) {
        enrichPatent(patent); //для корректного заполнения поля author у Patent
        patentRepository.save(patent);
    }

    //удаление в бд по переданному id
    @Transactional
    public void delete(int id) {
        getById(id); //получаю патент, если он есть, то удаляю, если нет - исключение
        patentRepository.deleteById(id);
    }

    //модификация бд
    @Transactional
    public void update(Patent updatePatent) {
        getById(updatePatent.getId()); //получаю патент, если он есть, то изменяю, если нет - исключение

        enrichPatent(updatePatent); //для корректного заполнения поля author у Patent

        //указав раннее setId, репозиторий понимает, что нужно обновить, а не сохранить
        patentRepository.save(updatePatent);
    }

    //чтение из бд
    //метод возвращает всех авторов
    public List<Patent> findAll() {
        return patentRepository.findAll();
    }

    //метод возаращет автора по id
    public Patent getById(int id) {
        Optional<Patent> patents = patentRepository.findById(id);
        //если автор есть в Optional, то возвращаем его, иначе null
        return patents.orElseThrow(PatnetNotFoundException::new);
    }

    //наполняю Patent, который пришел из DTO - он не полный, т.к. в нем есть только id автора, а не сам объект автор
    //по id получаю автора из сервиса и заполняю поле author в Patent
    private void enrichPatent(Patent patent) {
        int idAuthorFromPatent = patent.getAuthor().getId();
        patent.setAuthor(authorService.getById(idAuthorFromPatent));
    }

}
