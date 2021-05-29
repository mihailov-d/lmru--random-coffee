package ru.leroymerlin.random.coffee.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCreateRequest
import ru.leroymerlin.random.coffee.core.model.User
import ru.leroymerlin.random.coffee.core.repository.UserRepository
import ru.leroymerlin.random.coffee.core.service.UserService
import ru.leroymerlin.random.coffee.core.util.TgUserId
import java.util.UUID

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    override fun create(createReq: UserCreateRequest): User {
        val user = User(UUID.randomUUID(),
                createReq.telegramUserId)

        return userRepository.save(user)
    }

    override fun getByTelegramUserId(telegramUserId: TgUserId): User? {
        return try {
            userRepository.findByTelegramUserId(telegramUserId)
        } catch (ex: Exception) {
            log.debug("Cannot find user by tgUserId $telegramUserId")
            null
        }
    }

    override fun get(userId: UUID): User = userRepository.findOneById(userId)

    override fun getUserById(id: UUID): User {
        return userRepository.findOneById(id)
    }

    override fun update(updateReq: UserBasicUpdateRequest): User {
        val userEntity = userRepository.findOneById(updateReq.id)
                .copy(name = updateReq.name, surname = updateReq.surname)

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
                        telegramUsername = updateReq.telegramUsername,
                        preferCommunications = updateReq.preferCommunications)

        return userRepository.save(userEntity)
    }

    override fun delete(id: UUID) {
        userRepository.deleteById(id)
    }
}
