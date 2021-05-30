package ru.leroymerlin.random.coffee.core.exception

import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import java.lang.Exception

class CannotUpdateMeetingException(
    val oldStatus: MeetingStatusEnum, val newStatus: MeetingStatusEnum
): Exception() {
}