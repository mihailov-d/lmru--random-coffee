package ru.leroymerlin.random.coffee.core.dto.request

import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum
import ru.leroymerlin.random.coffee.core.util.UserId
import java.util.UUID

// Add other fields
data class UserCommunicationsUpdateRequest(
        override val id: UUID,
        val phone: String? = null,
        val email: String? = null,
        val telegram: UserId? = 0L,
        val preferCommunications: Set<UserPreferCommunicationEnum>? = null
) : IUserUpdateRequest
