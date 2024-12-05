package com.example.dataservice.service

import com.example.dataservice.dto.FIODto
import com.example.dataservice.dto.StudentDto
import com.example.dataservice.entity.Student
import com.example.dataservice.exception.StudentNotFoundException
import com.example.dataservice.repository.StudentRepository
import com.example.dataservice.toDto
import com.example.dataservice.toDocument
import org.springframework.stereotype.Service


@Service
class StudentService(private val studentRepository: StudentRepository) {

    fun saveStudent(studentDto: StudentDto,key: String) = studentRepository.save(studentDto.toDocument(key))

    fun saveStudent(student: Student):String {
        val st = studentRepository.findByRecordBook(student.recordBook)
        return if(st!=null) "Такой студент уже существует"
        else {
            studentRepository.save(student)
            "Судент успешно сохранён"
        }
    }



    fun getStudentWithGrades(studentId: String): StudentDto =
        studentRepository.findById(studentId)
            .orElseThrow {
                StudentNotFoundException("Student not found with id: $studentId")
            }.toDto()

    fun getAllStudents(): List<StudentDto> = studentRepository.findAll().map { it.toDto() }

    fun getStudentByFio(fioDto: FIODto): List<Student>? = studentRepository.findByFio(fioDto)

    fun getStudentRecordBoor(recordBook: Long): Student = studentRepository.findByRecordBook(recordBook)?:
    throw StudentNotFoundException("Student not found with recordBook: $recordBook")

    fun getExcellentStudents():List<StudentDto>? = getAllStudents().filter { it -> it.grades.all {it==5}}

    fun addGrades(recordBook: Long, grades: List<Int>) {
        val student = studentRepository.findByRecordBook(recordBook)?:
            throw StudentNotFoundException("Student not found with recordBook: $recordBook")
        student.grades += (grades)
        studentRepository.save(student)
    }

    fun contains(key: String ):Boolean = studentRepository.findByKey(key) != null

}

