package ru.leroymerlin.random.coffee.core.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.DELETED
import ru.leroymerlin.random.coffee.core.dto.request.MeetingCreateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.model.Meeting
import ru.leroymerlin.random.coffee.core.repository.MeetingRepository
import ru.leroymerlin.random.coffee.core.service.MeetingService
import java.time.LocalDateTime
import java.util.UUID

@Service
class MeetingServiceImpl : MeetingService {

    @Autowired
    internal lateinit var meetingRepository: MeetingRepository

    override fun create(createReq: MeetingCreateRequest): Meeting {
        val meeting = Meeting(
            UUID.randomUUID(),
            createReq.userId,
            topicTypeEnum = createReq.topicTypeEnum,
            // TODO when save in mongo we have minus 3 hours, because Moscow zoneID !!!!!
            preferDate = createReq.preferDate.atTime(12, 0)
        )

        return meetingRepository.save(meeting)
    }

    override fun update(updateReq: MeetingUpdateRequest): Meeting {
        val meetingEntity = meetingRepository.findOneById(updateReq.id)
            .copy(
                aim = updateReq.aim,
                comment = updateReq.comment,
                location = updateReq.location,
                locationType = updateReq.locationType,
                editedDate = LocalDateTime.now()
            )

        return meetingRepository.save(meetingEntity)
    }

    override fun getAllActiveMeetingByUser(id: UUID): Set<Meeting> {
        return meetingRepository.findAllByUserIdAndStatus(id, MeetingStatusEnum.ACTIVE)
    }

    override fun end(id: UUID) {
        val meetingEntity = meetingRepository.findOneById(id)
            .copy(status = MeetingStatusEnum.FINISHED)
        meetingRepository.save(meetingEntity)
    }

    override fun cancel(id: UUID) {
        val meetingEntity = meetingRepository.findOneById(id)
            .copy(status = MeetingStatusEnum.CANCELLED, editedDate = LocalDateTime.now())

        meetingRepository.save(meetingEntity)
    }

    override fun delete(id: UUID) {
        val meeting = meetingRepository.findOneById(id)
        meetingRepository.save(meeting.copy(status = DELETED, editedDate = LocalDateTime.now()))
    }


}
