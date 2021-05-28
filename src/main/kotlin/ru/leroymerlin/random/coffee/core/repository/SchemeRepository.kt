package ru.leroymerlin.random.coffee.core.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.leroymerlin.random.coffee.core.model.Meeting
import ru.leroymerlin.random.coffee.core.model.Session
import ru.leroymerlin.random.coffee.core.model.User
import java.util.UUID

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findOneById(id: UUID): User
}

@Repository
interface MeetingRepository : MongoRepository<Meeting, String> {
    fun findOneById(id: UUID): Meeting
}

@Repository
interface SessionRepository : MongoRepository<Session, String> {
    fun findOneById(id: UUID): Session
}




