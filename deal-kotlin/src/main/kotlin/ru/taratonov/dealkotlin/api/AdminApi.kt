package ru.taratonov.dealkotlin.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import ru.taratonov.dealkotlin.dto.ErrorDto
import ru.taratonov.dealkotlin.model.Application

@RequestMapping("/deal/admin/application")
@Tag(name = "Admin Controller", description = "Managing applications using db")
interface AdminApi {

    @GetMapping("/{applicationId}")
    @Operation(summary = "Get application by id", description = "Allows to get application by id")
    @ApiResponse(
        responseCode = "200",
        description = "Application received!",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Application::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun getApplication(
        @Parameter(description = "Id of the application", required = true)
        @PathVariable("applicationId") id: Long
    ): Application

    @GetMapping
    @Operation(summary = "Get all applications", description = "Allows to get all application from db")
    @ApiResponse(
        responseCode = "200",
        description = "Applications received!",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Application::class))]
    )
    fun getAllApplications(): List<Application>
}