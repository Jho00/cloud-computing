//package com.example.dataservice.config
//
//import com.example.dataservice.dto.StudentDto
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.annotation.EnableKafka
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
//import org.springframework.kafka.core.ConsumerFactory
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory
//import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
//import org.springframework.kafka.support.serializer.JsonDeserializer
//
//
//@EnableKafka
//@Configuration
//class KafkaConsumerConfig(@Value("\${spring.kafka.bootstrap-servers}")
//                          private val bootstrapAddress: String,
//                          @Value("\${kafka.consumer.id}")
//                          private val groupId: String) {
//
//    @Bean
//    fun consumerFactory(): ConsumerFactory<String, StudentDto> {
//        val props: MutableMap<String, Any?> = HashMap()
//        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
//        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
//        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
//
//        // Настройка десериализатора JsonDeserializer
//        val typeMapper = DefaultJackson2JavaTypeMapper()
//        typeMapper.addTrustedPackages("com.example.apiservice.dto", "com.example.dataService.dto")
//
//        val deserializer = JsonDeserializer(StudentDto::class.java)
//        deserializer.setTypeMapper(typeMapper)
//
//        // Установка десериализатора в конфигурацию
//        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = deserializer
//
//        return DefaultKafkaConsumerFactory(
//            props, StringDeserializer(), deserializer
//        )
//    }
//
//
//    @Bean
//    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, StudentDto> {
//        val factory = ConcurrentKafkaListenerContainerFactory<String, StudentDto>()
//        factory.consumerFactory = consumerFactory()
//        return factory
//    }
//}
//
