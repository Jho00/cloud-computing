package com.example.apiservice.dto

import com.example.apiservice.dto.annotation.DataClass
import jakarta.validation.constraints.Size

@DataClass
data class StudentDto (
    var fio: FIODto,
    val recordBook:Long,
    var grades: MutableList<Int> = mutableListOf(),
    var key:String
)


