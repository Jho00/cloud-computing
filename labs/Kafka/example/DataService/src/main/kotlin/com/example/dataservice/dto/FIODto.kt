package com.example.dataservice.dto

import com.example.dataservice.dto.annotation.DataClass

@DataClass
data class FIODto(
    var firstName: String,
    var lastName: String,
    var patronymic: String?
)