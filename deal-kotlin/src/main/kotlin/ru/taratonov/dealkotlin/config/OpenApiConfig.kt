package ru.taratonov.dealkotlin.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "Kotlin Deal REST Api",
        description = "2 MVP level deal",
        version = "1.0.0",
        contact = Contact(
            name = "Taratonov Vadim",
            email = "vtaratonov@neoflex.ru",
            url = "https://github.com/VadimTaratonov/credit-conveyor"
        )
    )
)
@Configuration
class OpenApiConfig