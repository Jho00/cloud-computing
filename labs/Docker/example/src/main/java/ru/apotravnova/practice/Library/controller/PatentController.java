package ru.apotravnova.practice.Library.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.apotravnova.practice.Library.dto.PatentDTO;
import ru.apotravnova.practice.Library.mapper.PatentMapper;
import ru.apotravnova.practice.Library.model.Author;
import ru.apotravnova.practice.Library.model.Patent;
import ru.apotravnova.practice.Library.service.PatentService;
import ru.apotravnova.practice.Library.util.AuthorCreatedException;
import ru.apotravnova.practice.Library.util.LibraryErrorResponce;
import ru.apotravnova.practice.Library.util.PatentCreatedException;

import java.util.List;
import java.util.stream.Collectors;

@RestController //все методы контроллера возвращают данные, а не представления
public class PatentController {

    //внедряем сервис
    private final PatentService patentService;

    @Autowired
    public PatentController(PatentService patentService) {
        this.patentService = patentService;
    }

    //метод возвращает список всех патентов
    @GetMapping("/patents")
    public List<PatentDTO> getPatents () {
        return patentService.findAll()
                .stream()
                .map(this::convertPatentToDto) //для каждого элемента листа вызваем метод конвератции
                .collect(Collectors.toList()); //перобразуем стрим к коллекции лист
    }

    //метод возвращает патент по id
    @GetMapping("/patent/{id}")
    public PatentDTO getPatent(@PathVariable("id") int id) {
        return convertPatentToDto(patentService.getById(id));
    }

    //метод удаления патента по id
    @DeleteMapping("/patent/{id}")
    public ResponseEntity<HttpStatus> deletePatent(@PathVariable("id") int id) {
        patentService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //метод создания патента
    @PostMapping("/patent")
    public ResponseEntity<HttpStatus> createPatent(@RequestBody @Valid PatentDTO patentDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage= getErrorMessageFromBindingResult(bindingResult);
            throw new PatentCreatedException(errorMessage);
        }

        patentService.save(convertDtoToPatent(patentDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //метод редактирования патента
    @PatchMapping("/patent")
    public ResponseEntity<HttpStatus> updatePatent(@RequestBody @Valid PatentDTO updatedPatentDTO,
                                                   BindingResult bindingResult) {
        //обработка ошибки
        if (bindingResult.hasErrors()) {
            String errorMessage = getErrorMessageFromBindingResult(bindingResult);
            //после того как поймали ошибку ее нужно передать в виде JSON клиенту обратно - метод handleException
            throw new PatentCreatedException(errorMessage);
        }

        //конвертирую PatentDTO в Patent
        patentService.update(convertDtoToPatent(updatedPatentDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

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
    //в параметрах указываем какое исключение ловим
    @ExceptionHandler
    private ResponseEntity<LibraryErrorResponce> handleException(Exception e) {
        LibraryErrorResponce responce = new LibraryErrorResponce(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(responce, HttpStatus.NOT_FOUND); //404
    }

    //конвертируем PatentDTO в Patent
    private Patent convertDtoToPatent(PatentDTO patentDTO) {
        return PatentMapper.INSTANCE.toModel(patentDTO);
    }

    //конвертируем Patent в PatentDTO
    private PatentDTO convertPatentToDto(Patent patent) {
        //используем внедренный маппер для сопоставления одного объекта модели другому
        return PatentMapper.INSTANCE.toDto(patent);
    }

}
