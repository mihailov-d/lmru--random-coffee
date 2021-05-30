package ru.leroymerlin.random.coffee.core.dto.request

import java.util.UUID

data class MeetingLinkUpdateRequest(val id: UUID, val requestLinkMeetingId: UUID)