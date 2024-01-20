package com.example.apiservice.controller

import com.example.apiservice.dto.FIODto
import com.example.apiservice.dto.StudentDto
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
class ApiServiceController(
    private val kafkaTemplate: KafkaTemplate<String, StudentDto>,
    private val restTemplate: RestTemplate,
    @Value("\${data-service.base-url}") private val dataServiceBaseUrl: String,
    @Value("\${kafka.student.topic}") private val studentTopic: String,
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String,
) {
    private val dataPath: String = "data"

    @PostMapping("/addStudent")
    fun addStudent(@Valid @RequestBody studentDto: StudentDto): ResponseEntity<String> {
        kafkaTemplate.send(studentTopic, studentDto)
        return ResponseEntity.ok("Student added successfully $bootstrapAddress")
    }

    @PostMapping("/addGrades/{studentId}")
    fun addGradesToStudent(@PathVariable studentId: Long, @RequestBody grades: List<Int>): ResponseEntity<String> {
        val dataUrl = "$dataServiceBaseUrl/$dataPath/addGrades/$studentId"

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity: HttpEntity<List<Int>> = HttpEntity(grades, headers)


        val responseEntity: ResponseEntity<String> = restTemplate.exchange(
            dataUrl,
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )

        return if (responseEntity.statusCode.is2xxSuccessful) {
            ResponseEntity.ok("Оценки успешно добавлены студенту")
        } else if (responseEntity.statusCode.is4xxClientError) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.status(responseEntity.statusCode).body("Что-то пошло не так")
        }
    }

    @GetMapping("/getStudentReport/{studentId}")
    fun getStudentReport(@PathVariable studentId: Long): ResponseEntity<StudentDto> {
        val dataServiceUrl = "$dataServiceBaseUrl/$dataPath/getStudent/$studentId"

        val report = restTemplate.getForObject(dataServiceUrl, StudentDto::class.java)
        return ResponseEntity.ok(report)
    }

    @GetMapping("/getAllStudents")
    fun getAllStudents(): ResponseEntity<List<StudentDto>> {
        val dataServiceUrl = "$dataServiceBaseUrl/$dataPath/getAllStudents"

        val responseType = object : ParameterizedTypeReference<List<StudentDto>>() {}
        val report = restTemplate.exchange(dataServiceUrl, HttpMethod.GET, null, responseType)

        return ResponseEntity.ok(report.body)
    }

    @GetMapping("/getExcellentStudents")
    fun getExcellentStudents(): ResponseEntity<List<StudentDto>> {
        val dataServiceUrl = "$dataServiceBaseUrl/$dataPath/getExcellentStudents"

        val responseType = object : ParameterizedTypeReference<List<StudentDto>>() {}
        val report = restTemplate.exchange(dataServiceUrl, HttpMethod.GET, null, responseType)

        return ResponseEntity.ok(report.body)
    }

    @GetMapping("/getStudentsByFio")
    fun getStudentsByFIO(@RequestBody fioDto: FIODto): ResponseEntity<List<StudentDto>?> {
        val dataServiceUrl = "$dataServiceBaseUrl/$dataPath/getStudentsByFIO"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val responseType = object : ParameterizedTypeReference<List<StudentDto>>() {}
        val report = restTemplate.exchange(dataServiceUrl, HttpMethod.GET, null, responseType)
        return ResponseEntity.ok(report.body)
    }
}