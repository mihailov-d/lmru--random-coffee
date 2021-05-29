package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.request.MeetingCreateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingLinkUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingRequestFromUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingRequestToUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.model.Meeting
import java.util.UUID

interface MeetingService {
    // Create meeting
    fun create(createReq: MeetingCreateRequest): Meeting
    // FIXME Change type to List from Set
    fun getAllActiveMeetingByUser(id: UUID):  Set<Meeting>
    fun end(id: UUID)
    // Cancel meeting
    fun cancel(id: UUID)
    // Delete meeting
    fun delete(id: UUID)
    // Active
    fun active(id: UUID): Meeting
    // Random
    fun random(id: UUID): Meeting
    // Approve
    fun update(updateReq: MeetingRequestFromUpdateRequest): Meeting
    fun update(updateReq: MeetingRequestToUpdateRequest): Meeting
    fun update(updateReq: MeetingLinkUpdateRequest): Meeting

}
