package ru.leroymerlin.random.coffee.core.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.leroymerlin.random.coffee.core.model.User

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findOneById(id: String): User
}

