package ru.taratonov.dealkotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.taratonov.dealkotlin.model.Client

@Repository
interface ClientRepository : JpaRepository<Client, Long> {
}