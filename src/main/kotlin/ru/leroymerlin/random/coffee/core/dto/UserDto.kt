package ru.leroymerlin.random.coffee.core.dto

import ru.leroymerlin.random.coffee.core.dto.UserStatusEnum.DRAFT
import java.util.UUID

// Add other fields
// future @Document
data class UserDto(
        // internal user id, uniq
    val id: UUID,
    val telegramUserId: Long,
    val name: String? = null,
    val surname: String? = null,
    val preferCommunications: Set<UserPreferCommunicationEnum>? = null,
    val email: String? = null,
    val phone: String? = null,
    val telegramUsername: String? = null,
    val aboutMe: String? = null,
    val aboutJob: String? = null,
    val status: UserStatusEnum = DRAFT
)
