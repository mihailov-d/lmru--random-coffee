package ru.leroymerlin.random.coffee.core.dto.request

import java.util.UUID

data class MeetingRequestFromUpdateRequest(val requestToMeetingId: UUID, val requestFromMeetingId: UUID)
