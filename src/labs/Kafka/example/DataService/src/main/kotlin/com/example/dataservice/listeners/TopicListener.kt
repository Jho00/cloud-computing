package com.example.dataservice.listeners

import com.example.dataservice.dto.StudentDto
import com.example.dataservice.service.StudentService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TopicListener(
    private val studentService: StudentService
) {

    @KafkaListener(topics = ["student-topic"], groupId = "group_id_1")
    fun consume(studentDto: StudentDto) {
        // Получаем данные из Kafka и сохраняем студента с оценками
        studentService.saveStudent(studentDto)
        println("Received message: ${studentService.saveStudent(studentDto)}")
    }
}