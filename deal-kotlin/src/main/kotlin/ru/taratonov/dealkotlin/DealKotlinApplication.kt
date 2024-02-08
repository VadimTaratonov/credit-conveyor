package ru.taratonov.dealkotlin

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
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
class DealKotlinApplication

fun main(args: Array<String>) {
    runApplication<DealKotlinApplication>(*args)
}
