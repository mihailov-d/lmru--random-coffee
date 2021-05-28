package ru.leroymerlin.random.coffee.core.dto.request

import java.util.UUID

// Add other fields
data class UserBasicUpdateRequest(override val id: UUID, val name: String?, val surname: String?) : IUserUpdateRequest
