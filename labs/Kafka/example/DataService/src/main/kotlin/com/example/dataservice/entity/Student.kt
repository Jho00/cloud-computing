package com.example.dataservice.entity

import com.example.dataservice.dto.FIODto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "student")
data class Student(
    @Id
    val id: String? = null,
    val fio: FIODto,
    @Indexed(unique = true)
    val recordBook:Long,
    val grades: MutableList<Int> = mutableListOf(),
    @Indexed(unique = true)
    var key:String
)