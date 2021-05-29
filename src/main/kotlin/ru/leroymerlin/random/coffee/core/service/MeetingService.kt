package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.request.MeetingCreateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.model.Meeting
import java.util.UUID

interface MeetingService {

    // internal meetingId
    fun get(meetingId: UUID): Meeting

    // Create meeting
    fun create(createReq: MeetingCreateRequest): Meeting
    // Update meeting
    fun update(updateReq: MeetingUpdateRequest): Meeting
    fun getAllActiveMeetingByUser(id: UUID): Set<Meeting>
    fun getMeetingsForUser(userId: UUID, statuses: Set<MeetingStatusEnum>): Set<Meeting>
    fun end(id: UUID)
    // Cancel meeting
    fun cancel(id: UUID)
    // Delete meeting
    fun delete(id: UUID)
}
