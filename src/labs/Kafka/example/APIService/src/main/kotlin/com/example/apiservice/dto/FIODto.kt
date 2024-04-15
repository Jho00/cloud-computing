package com.example.apiservice.dto

import com.example.apiservice.dto.annotation.DataClass

@DataClass
data class FIODto(
    var firstName: String,
    var lastName: String,
    var patronymic: String?
)