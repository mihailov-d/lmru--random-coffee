package ru.leroymerlin.random.coffee.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.ACTIVE
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.CANCELLED
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.CONFIRMED
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.DELETED
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.DRAFT
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.RANDOM
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.REQUEST
import ru.leroymerlin.random.coffee.core.dto.request.MeetingCreateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingLinkUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingRequestFromUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingRequestToUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.exception.CannotUpdateMeetingException
import ru.leroymerlin.random.coffee.core.model.Meeting
import ru.leroymerlin.random.coffee.core.repository.MeetingRepository
import ru.leroymerlin.random.coffee.core.service.MeetingService
import java.time.LocalDateTime
import java.util.UUID

@Service
class MeetingServiceImpl(
    private val meetingRepository: MeetingRepository
) : MeetingService {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
        val PROHIBITED_STATUSES = setOf(DELETED, CANCELLED, CONFIRMED)
    }
    @Autowired
    internal lateinit var meetingRepository: MeetingRepository
    override fun get(meetingId: UUID): Meeting = meetingRepository.findOneById(meetingId)

    override fun create(createReq: MeetingCreateRequest): Meeting {
        val meeting = Meeting(
            UUID.randomUUID(),
            createReq.userId,
            topicTypeEnum = createReq.topicTypeEnum,
            // TODO when save in mongo we have minus 3 hours, because Moscow zoneID !!!!!
            preferDate = createReq.preferDate.atTime(12, 0),
            status = RANDOM
        )
        return meetingRepository.save(meeting)
    }


    override fun getMeetingsForUser(userId: UUID, statuses: Set<MeetingStatusEnum>): Set<Meeting> {
        return statuses.map { meetingRepository.findAllByUserIdAndStatus(userId, it) }.flatten().toSet()
    }

    override fun getAllActiveMeetingByUser(id: UUID): Set<Meeting> {
        return meetingRepository.findAllByUserIdAndStatus(id, MeetingStatusEnum.ACTIVE)
    }

    override fun end(id: UUID) {
        val meetingEntity = meetingRepository.findOneById(id)
                .copy(status = MeetingStatusEnum.FINISHED, editedDate = LocalDateTime.now())
        // TODO send message to other interlocutor
        meetingRepository.save(meetingEntity)
    }

    override fun cancel(id: UUID) {
        val meetingEntity = meetingRepository.findOneById(id)
            .copy(status = MeetingStatusEnum.CANCELLED, editedDate = LocalDateTime.now())
        // TODO send message to other interlocutor if exist
        meetingRepository.save(meetingEntity)
    }

    override fun delete(id: UUID) {
        // FIXME maybe it does not need
        meetingRepository.save(
            meetingRepository.findOneById(id).copy(status = DELETED, editedDate = LocalDateTime.now())
        )
    }

    override fun active(id: UUID): Meeting {
        val meeting = meetingRepository.findOneById(id)
        if (meeting.status !in setOf(DRAFT, REQUEST, RANDOM) || meeting.status in PROHIBITED_STATUSES) {
            log.debug("Cannot update status of meeting $id from ${meeting.status} to $ACTIVE")
            throw CannotUpdateMeetingException(meeting.status, ACTIVE)
        }
        return meetingRepository.save(
            meeting.copy(
                status = ACTIVE,
                requestFromMeetingId = null,
                requestToMeetingId = null,
                editedDate = LocalDateTime.now()
            )
        )
    }

    override fun random(id: UUID): Meeting {
        val meeting = meetingRepository.findOneById(id)
        if ((meeting.status == RANDOM && meeting.requestToMeetingId != null)
            || (meeting.status == REQUEST) || meeting.status in PROHIBITED_STATUSES
        ) {
            log.debug("Cannot update status of meeting $id from ${meeting.status} to $RANDOM")
            throw CannotUpdateMeetingException(meeting.status, RANDOM)
        }
        return meetingRepository.save(meeting.copy(status = RANDOM, editedDate = LocalDateTime.now()))
    }

    override fun update(updateReq: MeetingRequestFromUpdateRequest): Meeting {
        val meeting = meetingRepository.findOneById(updateReq.id)
        if (meeting.status == ACTIVE) {
            return meetingRepository.save(
                meeting.copy(
                    requestFromMeetingId = updateReq.requestFromMeetingId,
                    status = REQUEST,
                    editedDate = LocalDateTime.now()
                )
            )
        }
        log.debug("Cannot update status of meeting ${updateReq.id} from ${meeting.status} to $REQUEST")
        throw CannotUpdateMeetingException(meeting.status, REQUEST)
    }

    override fun update(updateReq: MeetingRequestToUpdateRequest): Meeting {
        val meeting = meetingRepository.findOneById(updateReq.id)
        if (meeting.status == RANDOM) {
            return meetingRepository.save(
                meeting.copy(
                    requestToMeetingId = updateReq.requestToMeetingId,
                    editedDate = LocalDateTime.now()
                )
            )
        }
        log.debug("Cannot update status of meeting ${updateReq.id} from ${meeting.status} to ${RANDOM}")
        throw CannotUpdateMeetingException(meeting.status, RANDOM)
    }

    override fun update(updateReq: MeetingLinkUpdateRequest): Meeting {
        val meeting = meetingRepository.findOneById(updateReq.id)
        if (meeting.status == RANDOM) {
            return meetingRepository.save(
                meeting.copy(
                    linkMeetingId = updateReq.requestLinkMeetingId,
                    status = CONFIRMED,
                    editedDate = LocalDateTime.now()
                )
            )
        }
        log.debug("Cannot update status of meeting ${updateReq.id} from ${meeting.status} to $CONFIRMED")
        throw CannotUpdateMeetingException(meeting.status, CONFIRMED)
    }
}
