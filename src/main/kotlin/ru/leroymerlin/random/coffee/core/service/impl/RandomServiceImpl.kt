package ru.leroymerlin.random.coffee.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.RANDOM
import ru.leroymerlin.random.coffee.core.dto.request.MeetingLinkUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingRequestFromUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.MeetingRequestToUpdateRequest
import ru.leroymerlin.random.coffee.core.model.Meeting
import ru.leroymerlin.random.coffee.core.repository.MeetingRepository
import ru.leroymerlin.random.coffee.core.service.MeetingService
import ru.leroymerlin.random.coffee.core.service.RandomService
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.service.UserService
import ru.leroymerlin.random.coffee.core.tg.sender.MeetingRequestSender
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import javax.annotation.PostConstruct

@Service
class RandomServiceImpl : RandomService {

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    private lateinit var sessionService: SessionService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var meetingRequestSender: MeetingRequestSender

    @Autowired
    private lateinit var meetingRepository: MeetingRepository

    val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    val randomMeetingQueue = LinkedBlockingQueue<Meeting>()

    init {
        executorService.schedule(::loadRandom, 10, SECONDS)
        executorService.scheduleWithFixedDelay(::process, 15, 10, TimeUnit.SECONDS)
    }

    @PostConstruct
    fun init() {
        meetingRepository.findAll().filter { it.status == MeetingStatusEnum.DRAFT }.forEach {
            val updatedMeeting = meetingRepository.save(it.copy(status = RANDOM))
            random(updatedMeeting)
        }
    }

    override fun random(meeting: Meeting) {
        randomMeetingQueue.put(meeting)
    }

    private fun process() {
        while (!randomMeetingQueue.isNullOrEmpty()) {
            val randomMeeting = randomMeetingQueue.poll(1, SECONDS)
            val expectedMeetings = meetingService.findAllActiveByPreferDateAndTopicTypeEnum(
                    randomMeeting.preferDate,
                    randomMeeting.topicTypeEnum
            ).toSet()
            if (expectedMeetings.isEmpty()) {
                meetingService.active(randomMeeting.id)
                break
            }
            expectedMeetings.firstOrNull { it.userId != randomMeeting.userId }?.let { requestMeeting ->
                try {
                    meetingService.update(MeetingRequestFromUpdateRequest(requestMeeting.id, randomMeeting.id))
                    log.info("Send request to meeting ${requestMeeting.id}")
                } catch (ex: Exception) {
                    meetingService.active(randomMeeting.id)
                    false
                }
                meetingService.update(MeetingRequestToUpdateRequest(randomMeeting.id, requestMeeting.id))
                val randomUser = userService.getUserById(randomMeeting.userId)
                val requestUser = userService.getUserById(requestMeeting.userId)
                val randomUserSession = sessionService.getState(randomUser.telegramUserId!!)
                val requestUserSession = sessionService.getState(requestUser.telegramUserId!!)
                meetingRequestSender.sendPropose(requestUserSession!!.telegramChatId, randomMeeting.id)
                true
            }
        }
        log.info("Finish random process")
    }

    private fun loadRandom() {
        val randomMeetings = meetingService.findAllRandomMeetings()
        randomMeetingQueue.addAll(randomMeetings)
    }

    override fun approve(meetingId: UUID) {
        val requestMeeting = meetingService.get(meetingId)
        val randomMeeting = meetingService.get(if (requestMeeting.status == RANDOM) requestMeeting.requestToMeetingId!! else requestMeeting.requestFromMeetingId!!)
        meetingService.update(MeetingLinkUpdateRequest(requestMeeting.id, randomMeeting.id))
        meetingService.update(MeetingLinkUpdateRequest(randomMeeting.id, requestMeeting.id))
    }

    override fun nope(meetingId: UUID) {
        val requestMeeting = meetingService.get(meetingId)
        val randomMeeting = meetingService.get(if (requestMeeting.status == RANDOM) requestMeeting.requestToMeetingId!! else requestMeeting.requestFromMeetingId!!)
        meetingService.active(requestMeeting.id)
        meetingService.active(randomMeeting.id)
    }
}
