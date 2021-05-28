package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCreateRequest
import ru.leroymerlin.random.coffee.core.model.User
import java.util.UUID

interface UserService {
    // Create user
    fun create(createReq: UserCreateRequest): User

    // Update user
    fun update(updateReq: UserBasicUpdateRequest): User
    fun update(updateReq: UserAboutUpdateRequest): User
    fun update(updateReq: UserCommunicationsUpdateRequest): User

    // Delete user
    fun delete(id: UUID)
}
