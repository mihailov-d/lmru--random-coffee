package ru.leroymerlin.random.coffee.core.dto.request

import java.util.UUID

// Add other fields
data class UserAboutUpdateRequest(override val id: UUID, val aboutMe: String? = null, val aboutJob: String? = null) :
    IUserUpdateRequest
