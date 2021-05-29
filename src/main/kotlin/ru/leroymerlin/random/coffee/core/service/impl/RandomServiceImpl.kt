package ru.leroymerlin.random.coffee.core.service.impl

import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.model.Meeting
import ru.leroymerlin.random.coffee.core.service.MeetingService
import ru.leroymerlin.random.coffee.core.service.RandomService
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.service.UserService

@Service
class RandomServiceImpl(
    private val sessionService: SessionService,
    private val userService: UserService,
    private val meetingService: MeetingService
): RandomService {
    override fun random(meeting: Meeting) {
        TODO("Not yet implemented")
    }

    override fun pool(meeting: Meeting) {
        TODO("Not yet implemented")
    }
}