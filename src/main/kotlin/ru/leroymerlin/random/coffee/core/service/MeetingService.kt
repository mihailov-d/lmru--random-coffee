package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.MeetingDto
import ru.leroymerlin.random.coffee.core.dto.request.MeetingCreateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import java.util.UUID

interface MeetingService {
    // Create meeting
    fun create(createReq: MeetingCreateRequest): MeetingDto
    // Update meeting
    fun update(updateReq: MeetingUpdateRequest): MeetingDto
    // Cancel meeting
    fun cancel(id: UUID)
    // Delete meeting
    fun delete(id: UUID)
}