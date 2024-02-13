package ru.taratonov.dealkotlin.service

import ru.taratonov.dealkotlin.model.Application

interface AdminService {
    fun getApplicationById(id: Long): Application

    fun getAllApplications(): List<Application>
}