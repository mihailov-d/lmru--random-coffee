package ru.leroymerlin.random.coffee.core.dto.request

import ru.leroymerlin.random.coffee.core.dto.LocationTypeEnum
import java.time.LocalDate
import java.util.UUID

data class MeetingUpdateRequest(
    val id: UUID,
    val preferDate: LocalDate?,
    val aim: String?,
    val locationType: LocationTypeEnum?,
    val location: String?,
    val comment: String = ""
)
