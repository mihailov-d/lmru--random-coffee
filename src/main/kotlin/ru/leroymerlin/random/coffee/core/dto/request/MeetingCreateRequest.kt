package ru.leroymerlin.random.coffee.core.dto.request

import java.time.LocalDate
import java.util.UUID

data class MeetingCreateRequest(val userId: UUID, val topicTypeEnum: TopicTypeEnum, val preferDate: LocalDate)
