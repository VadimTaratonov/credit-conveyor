package ru.taratonov.conveyor.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "Kotlin Conveyor REST Api",
        description = "1 MVP level conveyor",
        version = "1.0.0",
        contact = Contact(
            name = "Taratonov Vadim",
            email = "vtaratonov@neoflex.ru",
            url = "https://github.com/VadimTaratonov/credit-conveyor"
        )
    )
)
@Configuration
class OpenApiConfig {
}