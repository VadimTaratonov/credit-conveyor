package ru.taratonov.dealkotlin.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.taratonov.dealkotlin.service.DocumentKafkaService

@RestController
class DocumentKafkaController(private val documentKafkaService: DocumentKafkaService):DocumentKafkaApi {
    override fun sendDocuments(id: Long): ResponseEntity<HttpStatus> {
        documentKafkaService.sendDocuments(id)
        return ResponseEntity.ok(HttpStatus.OK)
    }

    override fun requestSignDocument(id: Long): ResponseEntity<HttpStatus> {
        documentKafkaService.requestSignDocument(id)
        return ResponseEntity.ok(HttpStatus.OK)
    }

    override fun signDocumentWithCode(id: Long, sesCode: Int): ResponseEntity<HttpStatus> {
        documentKafkaService.signDocument(id, sesCode)
        return ResponseEntity.ok(HttpStatus.OK)
    }

    override fun denyApplication(id: Long): ResponseEntity<HttpStatus> {
        documentKafkaService.denyApplication(id)
        return ResponseEntity.ok(HttpStatus.OK)
    }
}