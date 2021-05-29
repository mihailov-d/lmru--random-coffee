package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.model.Meeting
import java.util.UUID

interface RandomService {

    fun random(meeting: Meeting)
    fun approve(meetingId: UUID)
    fun nope(meetingId: UUID)
}