package ru.taratonov.dealkotlin.api

import org.springframework.web.bind.annotation.RestController
import ru.taratonov.dealkotlin.model.Application
import ru.taratonov.dealkotlin.service.AdminService

@RestController
class AdminController(private val adminService: AdminService) : AdminApi {
    override fun getApplication(id: Long): Application {
        return adminService.getApplicationById(id)
    }

    override fun getAllApplications(): List<Application> {
        return adminService.getAllApplications()
    }
}