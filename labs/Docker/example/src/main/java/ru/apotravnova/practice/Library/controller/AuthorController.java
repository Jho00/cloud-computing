package ru.apotravnova.practice.Library.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.apotravnova.practice.Library.dto.AuthorDTO;
import ru.apotravnova.practice.Library.mapper.AuthorMapper;
import ru.apotravnova.practice.Library.model.Author;
import ru.apotravnova.practice.Library.service.AuthorService;
import ru.apotravnova.practice.Library.util.AuthorCreatedException;
import ru.apotravnova.practice.Library.util.LibraryErrorResponce;

import java.util.List;
import java.util.stream.Collectors;

@RestController //все методы контроллера возвращают данные, а не представления
//@RequestMapping("/authors")
public class AuthorController {

    //внедряем сервис и маппер (для преобразования DTO в модель и обратно)
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    //метод возвращает список всех атворов
    @GetMapping("/authors")
    public List<AuthorDTO> getAuthors() {
        return authorService.findAll()
                .stream()
                .map(this::convertAuthorToDto) //для каждого элемента листа вызваем метод конвератции
                .collect(Collectors.toList()); //перобразуем стрим к коллекции лист
    }

    //метод возвращает автора по id
    @GetMapping("/author/{id}")
    public AuthorDTO getAuthor(@PathVariable("id") int id) {
        return convertAuthorToDto(authorService.getById(id));
    }

    //метод удаляет автора по id
    @DeleteMapping("/author/{id}")
    public ResponseEntity<HttpStatus> deleteAuthor(@PathVariable int id) {
        authorService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //метод создания автора
    @PostMapping("/author")
    public ResponseEntity<HttpStatus> createAuthor(@RequestBody @Valid AuthorDTO authorDTO,
                                                   BindingResult bindingResult) {
        //обработка ошибки
        if (bindingResult.hasErrors()) {
            String errorMessage = getErrorMessageFromBindingResult(bindingResult);
            //после того как поймали ошибку ее нужно передать в виде JSON клиенту обратно - метод handleException
            throw new AuthorCreatedException(errorMessage);
        }

        //конвертирую AuthorDTO в Author
        authorService.save(convertDtoToAuthor(authorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //метод редактирования автора
//    @PatchMapping("/author/{id}")
//    public ResponseEntity<HttpStatus> updateAuthor(@PathVariable("id") int id,
//                                                   @RequestBody @Valid AuthorDTO updatedAuthorDTO,
//                                                   BindingResult bindingResult) {
    @PatchMapping("/author")
    public ResponseEntity<HttpStatus> updateAuthor(@RequestBody @Valid AuthorDTO updatedAuthorDTO,
                                                   BindingResult bindingResult) {
        //обработка ошибки
        if (bindingResult.hasErrors()) {
            String errorMessage = getErrorMessageFromBindingResult(bindingResult);
            //после того как поймали ошибку ее нужно передать в виде JSON клиенту обратно - метод handleException
            throw new AuthorCreatedException(errorMessage);
        }

        //конвертирую AuthorDTO в Author
        authorService.update(convertDtoToAuthor(updatedAuthorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //обработка ошибки при валидации полей
    private String getErrorMessageFromBindingResult(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        //коллекция ошибок
        List<FieldError> errors = (List<FieldError>) bindingResult.getFieldError();
        for (FieldError error : errors) {
            errorMessage.append(error.getField()) //ошибка
                    .append(" - ").append(error.getDefaultMessage()) //текст ошибки
                    .append(";");
        }
        return errorMessage.toString();
    }

    //метод ловит исключение при СОЗДАНИИ и возвращает необходимый JSON
    //в параметрах указываем какое исключение ловим (я передала общее, чтобы не дублировать код)
    @ExceptionHandler
    private ResponseEntity<LibraryErrorResponce> handleException(Exception e) {
        LibraryErrorResponce responce = new LibraryErrorResponce(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND); //404
    }

    //конвертируем AuthorDTO в Author
    private Author convertDtoToAuthor(AuthorDTO authorDTO)  {
        //используем внедренный маппер для сопоставления одного объекта модели другому
        return AuthorMapper.INSTANCE.toModel(authorDTO);//modelMapper.map(authorDTO, Author.class);
    }

    //конвертируем Author в AuthorDTO
    //использую созданный маппер
    private AuthorDTO convertAuthorToDto(Author author)  {
        //используем внедренный маппер для сопоставления одного объекта модели другому
        return AuthorMapper.INSTANCE.toDto(author);//modelMapper.map(author, AuthorDTO.class);
    }
}

