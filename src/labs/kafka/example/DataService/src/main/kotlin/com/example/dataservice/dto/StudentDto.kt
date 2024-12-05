package com.example.dataservice.dto

import com.example.dataservice.dto.annotation.DataClass
import com.example.dataservice.dto.FIODto

@DataClass
data class StudentDto (
    val fio: FIODto,
    val recordBook:Long,
    val grades: MutableList<Int> = mutableListOf()
)


