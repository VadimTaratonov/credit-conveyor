package ru.taratonov.dealkotlin.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.taratonov.dealkotlin.dto.ErrorDto

@RequestMapping("/deal/document")
@Tag(name = "Document Controller", description = "Provides the ability to manage documents")
interface DocumentKafkaApi {

    @PostMapping("/{applicationId}/send")
    @Operation(summary = "Send documents", description = "Allows to send documents to client email")
    @ApiResponse(
        responseCode = "200",
        description = "Document has been sent!",
        content = [Content(mediaType = "application/json")]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun sendDocuments(
        @Parameter(description = "Id of the application", required = true)
        @PathVariable("applicationId") id: Long
    ): ResponseEntity<HttpStatus>

    @PostMapping("/{applicationId}/sign")
    @Operation(summary = "Request to sign documents", description = "Allows to send special code for singing documents")
    @ApiResponse(
        responseCode = "200",
        description = "Code has been sent!",
        content = [Content(mediaType = "application/json")]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun requestSignDocument(
        @Parameter(description = "Id of the application", required = true)
        @PathVariable("applicationId") id: Long
    ): ResponseEntity<HttpStatus>

    @PostMapping("/{applicationId}/code")
    @Operation(
        summary = "Sign document with code",
        description = "Allows check special code and send email with successful loan request"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Credit issued!",
        content = [Content(mediaType = "application/json")]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun signDocumentWithCode(
        @Parameter(description = "Id of the application", required = true)
        @PathVariable("applicationId") id: Long,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "SES code")
        @RequestBody sesCode: Int
    ): ResponseEntity<HttpStatus>

    @PostMapping("/{applicationId}/deny")
    @Operation(summary = "Deny application", description = "Allows to deny the application")
    @ApiResponse(
        responseCode = "200",
        description = "Application denied!",
        content = [Content(mediaType = "application/json")]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun denyApplication(
        @Parameter(description = "Id of the application", required = true)
        @PathVariable("applicationId") id: Long
    ): ResponseEntity<HttpStatus>
}