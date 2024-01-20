package com.example.dataservice

import com.example.dataservice.dto.StudentDto
import com.example.dataservice.entity.Student

fun Student.toDto():StudentDto = StudentDto(fio,recordBook,grades)

fun StudentDto.toDocument(key:String): Student = Student(null,fio,recordBook,grades,key)