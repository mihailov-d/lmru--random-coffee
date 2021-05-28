package ru.leroymerlin.random.coffee.core.dto

import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.DRAFT
import ru.leroymerlin.random.coffee.core.dto.request.TopicTypeEnum
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class MeetingDto(
        val id: UUID,
        val userId: UUID,
        val createdDate: LocalDateTime,
        val topicType: TopicTypeEnum,
        val preferDate: LocalDate,
        val updatedDate: LocalDateTime,
        val aim: String? = null,
        val comment: String? = null,
        val locationType: LocationTypeEnum? = null,
        val location: String? = null,
        val status: MeetingStatusEnum = DRAFT
)
