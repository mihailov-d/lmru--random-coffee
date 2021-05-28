package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.request.UserCreateRequest
import ru.leroymerlin.random.coffee.core.dto.UserDto
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import java.util.UUID

interface UserService {
    // Create user
    fun create(createReq: UserCreateRequest): UserDto
    // Update user
    fun update(updateReq: UserBasicUpdateRequest): UserDto
    fun update(updateReq: UserAboutUpdateRequest): UserDto
    fun update(updateReq: UserCommunicationsUpdateRequest): UserDto
    // Delete user
    fun delete(id: UUID)
}