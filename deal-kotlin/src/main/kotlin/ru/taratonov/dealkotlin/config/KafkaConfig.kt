package ru.taratonov.dealkotlin.config

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConfig(
    @Value("\${spring.kafka.producer.bootstrap-servers}") private val BOOTSTRAP_SERVERS: String
) {
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs = mutableMapOf<String, Any>()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
        return KafkaAdmin(configs)
    }

    @Bean
    fun finishRegistration(): NewTopic {
        return NewTopic("finish-registration",1, 1)
    }

    @Bean
    fun createDocuments(): NewTopic {
        return NewTopic("create-documents",1, 1)
    }

    @Bean
    fun sendDocuments(): NewTopic {
        return NewTopic("send-documents",1, 1)
    }

    @Bean
    fun sendSes(): NewTopic {
        return NewTopic("send-ses",1, 1)
    }

    @Bean
    fun creditIssued(): NewTopic {
        return NewTopic("credit-issued",1, 1)
    }

    @Bean
    fun applicationDenied(): NewTopic {
        return NewTopic("application-denied",1, 1)
    }

    @Bean
    fun audit(): NewTopic {
        return NewTopic("audit",1, 1)
    }

    @Bean
    fun adminClient(): AdminClient {
        return AdminClient.create(kafkaAdmin().configurationProperties)
    }
}