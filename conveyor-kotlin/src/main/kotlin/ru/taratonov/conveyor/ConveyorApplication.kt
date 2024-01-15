package ru.taratonov.conveyor

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
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
class ConveyorApplication

fun main(args: Array<String>) {
    runApplication<ConveyorApplication>(*args)
}
