package com.example.dataservice.controller

import com.example.dataservice.dto.FIODto
import com.example.dataservice.dto.StudentDto
import com.example.dataservice.exception.StudentNotFoundException
import com.example.dataservice.service.StudentService
import com.example.dataservice.toDocument
import com.example.dataservice.toDto
import com.mongodb.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/data")
class DataServiceController(private val studentService: StudentService) {


    @GetMapping("/getStudent/{recordBook}")
    fun getStudent(@PathVariable recordBook: Long): ResponseEntity<StudentDto> {
        // Получение студента по его идентификатору с использованием StudentService
        return try {
            val studentDto = studentService.getStudentRecordBoor(recordBook).toDto()
            ResponseEntity.ok(studentDto)
        } catch (e: StudentNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/addGrades/{recordBook}")
    fun addGrades(@PathVariable recordBook: Long,@RequestBody grades:List<Int>): ResponseEntity<String> {
        // Получение студента по его идентификатору с использованием StudentService
        return try {
            studentService.addGrades(recordBook,grades)
            ResponseEntity.ok("Оценки успешно добавлены")
        } catch (e: StudentNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/addStudent")
    fun addStudent(@RequestParam key: String, @RequestBody studentDto: StudentDto): ResponseEntity<String> {

        return try {
            studentService.saveStudent(studentDto, key)
            ResponseEntity.ok("Студент успешно сохранён")
        }
        catch (e: DuplicateKeyException){
            ResponseEntity.status(HttpStatus.CONFLICT).body("Студент уже существует")
        }
    }

    @GetMapping("/getAllStudents")
    fun getAllStudents(): ResponseEntity<List<StudentDto>> =
            ResponseEntity.ok(studentService.getAllStudents())


    @GetMapping("/getStudentsByFIO")
    fun getStudentsByFIO(@RequestBody fioDto: FIODto): ResponseEntity<List<StudentDto>> {
        val students = studentService.getStudentByFio(fioDto)?.map { it.toDto() } // Преобразуем список в DTO и исключаем null значения
        return ResponseEntity.ok(students)
    }
    @GetMapping("/getExcellentStudents")
    fun getExcellentStudents(): ResponseEntity<List<StudentDto>?> = ResponseEntity.ok(studentService.getExcellentStudents())


}

