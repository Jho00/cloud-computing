package com.example.dataservice.repository

import com.example.dataservice.dto.FIODto
import com.example.dataservice.entity.Student
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface StudentRepository : MongoRepository<Student, String> {

    fun findByFio(fio: FIODto): List<Student>?
    fun findByRecordBook(recordBook: Long): Student?
    fun findByKey(key: String): Student?
// Дополнительные методы, если нужны
}