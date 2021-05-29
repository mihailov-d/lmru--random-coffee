package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCreateRequest
import ru.leroymerlin.random.coffee.core.model.User
import ru.leroymerlin.random.coffee.core.util.TgUserId
import java.util.UUID

interface UserService {
    // Create user
    fun create(createReq: UserCreateRequest): User

    fun getByTelegramUserId(telegramUserId: TgUserId): User?
    fun get(userId: UUID): User
    fun getUserById(id: UUID): User

    // Update user
    fun update(updateReq: UserBasicUpdateRequest): User
    fun update(updateReq: UserAboutUpdateRequest): User
    fun update(updateReq: UserCommunicationsUpdateRequest): User

    // Delete user
    fun delete(id: UUID)
}
