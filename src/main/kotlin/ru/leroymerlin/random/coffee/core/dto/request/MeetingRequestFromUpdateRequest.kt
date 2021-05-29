package ru.leroymerlin.random.coffee.core.dto.request

import java.util.UUID

data class MeetingRequestFromUpdateRequest(val id: UUID, val requestFromMeetingId: UUID)