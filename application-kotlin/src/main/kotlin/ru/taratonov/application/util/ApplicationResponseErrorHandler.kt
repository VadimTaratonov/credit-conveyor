package ru.taratonov.application.util

import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import ru.taratonov.application.dto.ErrorDto
import ru.taratonov.application.exception.IllegalDataFromOtherMsException

class ApplicationResponseErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean {
        return response.statusCode.is4xxClientError
    }

    override fun handleError(response: ClientHttpResponse) {
        val objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build()
        val error = objectMapper.readValue(response.body, ErrorDto::class.java)
        throw IllegalDataFromOtherMsException(error.msg)
    }
}