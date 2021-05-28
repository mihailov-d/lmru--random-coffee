package ru.leroymerlin.random.coffee.core.repository.dao

import org.springframework.data.mongodb.repository.MongoRepository
import ru.leroymerlin.random.coffee.core.model.User

    interface UserDAO:MongoRepository<User,String>  {
        fun findOneById(id:String):User
    }
