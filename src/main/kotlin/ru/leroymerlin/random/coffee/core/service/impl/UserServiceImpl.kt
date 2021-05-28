package ru.leroymerlin.random.coffee.core.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCreateRequest
import ru.leroymerlin.random.coffee.core.model.User
import ru.leroymerlin.random.coffee.core.repository.UserRepository
import ru.leroymerlin.random.coffee.core.service.UserService
import java.util.UUID

@Service
class UserServiceImpl : UserService {

    @Autowired
    internal lateinit var userRepository : UserRepository

    override fun create(createReq: UserCreateRequest): User {
        val user = User(UUID.randomUUID(),
        createReq.telegramUserId)

        return userRepository.save(user)
    }

    override fun update(updateReq: UserBasicUpdateRequest): User {
        val userEntity = userRepository.findOneById(updateReq.id)
            .copy(name=updateReq.name, surname = updateReq.surname)

        return userRepository.save(userEntity)
    }

    override fun update(updateReq: UserAboutUpdateRequest): User {
        val userEntity = userRepository.findOneById(updateReq.id)
            .copy(aboutJob = updateReq.aboutJob, aboutMe = updateReq.aboutMe)

        return userRepository.save(userEntity)
    }

    override fun update(updateReq: UserCommunicationsUpdateRequest): User {
        val userEntity = userRepository.findOneById(updateReq.id)
            .copy(email = updateReq.email,
                phone = updateReq.phone,
                telegramUserId = updateReq.telegram,
                preferCommunications = updateReq.preferCommunications)

        return userRepository.save(userEntity)
    }

    override fun delete(id: UUID) {
        userRepository.deleteById(id)
    }
}
