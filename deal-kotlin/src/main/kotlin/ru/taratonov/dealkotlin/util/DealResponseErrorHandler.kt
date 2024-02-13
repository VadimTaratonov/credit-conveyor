package ru.taratonov.dealkotlin.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import ru.taratonov.dealkotlin.dto.ErrorDto
import ru.taratonov.dealkotlin.exception.IllegalDataFromOtherMsException

class DealResponseErrorHandler: ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean {
        return response.statusCode.is4xxClientError
    }

    override fun handleError(response: ClientHttpResponse) {
        val objectMapper = ObjectMapper()
        val error = objectMapper.readValue(response.body, ErrorDto::class.java)
        throw IllegalDataFromOtherMsException(error.msg)
    }
}