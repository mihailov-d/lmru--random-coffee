package ru.leroymerlin.random.coffee.core.dto

import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest


data class SessionDto(
    val id: String,
    val action: String,
    val draftUser: UserBasicUpdateRequest,
    val draftMeeting: MeetingUpdateRequest
)