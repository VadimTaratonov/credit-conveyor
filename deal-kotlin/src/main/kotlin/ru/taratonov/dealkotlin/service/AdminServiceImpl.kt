package ru.taratonov.dealkotlin.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.taratonov.dealkotlin.model.Application
import ru.taratonov.dealkotlin.repository.ApplicationRepository
import ru.taratonov.dealkotlin.util.notFound

@Service
class AdminServiceImpl(
    private val applicationRepository: ApplicationRepository
) : AdminService {
    private val logger = KotlinLogging.logger { }

    override fun getApplicationById(id: Long): Application {
        val foundApplication = applicationRepository.findById(id)
        if (foundApplication.isEmpty) {
            notFound("Application with id $id not found")
        }
        val application = foundApplication.get()
        logger.info("Application with id={} receive", id)
        return application
    }

    override fun getAllApplications(): List<Application> {
        val applications = applicationRepository.findAll()
        logger.info("Applications receive")
        return applications
    }
}