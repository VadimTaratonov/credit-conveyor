package ru.taratonov.dealkotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.taratonov.dealkotlin.model.Application

@Repository
interface ApplicationRepository : JpaRepository<Application, Long> {
}