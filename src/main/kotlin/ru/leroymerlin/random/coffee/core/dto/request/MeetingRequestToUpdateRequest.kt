package ru.leroymerlin.random.coffee.core.dto.request

import java.util.UUID

data class MeetingRequestToUpdateRequest(val id: UUID, val requestToMeetingId: UUID)