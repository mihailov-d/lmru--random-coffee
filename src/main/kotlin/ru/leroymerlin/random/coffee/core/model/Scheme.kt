package ru.leroymerlin.random.coffee.core.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.LocationTypeEnum
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum
import ru.leroymerlin.random.coffee.core.dto.UserStatusEnum
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.TopicTypeEnum
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.util.TgChatId
import ru.leroymerlin.random.coffee.core.util.TgUserId
import java.time.LocalDateTime
import java.util.UUID


@Document data class User(
        @Id val id: UUID,
        val telegramUserId: TgUserId? = 0L,
        val name: String? = null,
        val surname: String? = null,
        val preferCommunications: Set<UserPreferCommunicationEnum>? = null,
        val email: String? = null,
        val phone: String? = null,
        val telegramUsername: String? = null,
        val aboutMe: String? = null,
        val aboutJob: String? = null,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val editedDate: LocalDateTime = LocalDateTime.now(),
        val status: UserStatusEnum = UserStatusEnum.DRAFT
)

@Document data class Session(
        @Id val id: UUID,
        val userId: UUID,
        val tgUserId: TgUserId,
        val tgChatId: TgChatId,
        val currentChatState: ChatState = ChatState.NONE,
        val draftBasicUser: UserBasicUpdateRequest? = null,
        val draftMeeting: MeetingUpdateRequest? = null,
        val draftCommunicationUser: UserCommunicationsUpdateRequest? = null,
        val draftAboutUser: UserAboutUpdateRequest? = null,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val editedDate: LocalDateTime = LocalDateTime.now()

)

@Document data class Meeting(
        @Id val id: UUID,
        val userId: UUID,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val editedDate: LocalDateTime = LocalDateTime.now(),
        val topicTypeEnum: TopicTypeEnum,
        // cause problem when save LocalDate in mongo
        val preferDate: LocalDateTime,
        val aim: String? = null,
        val comment: String? = null,
        val locationType: LocationTypeEnum? = null,
        val location: String? = null,
        val requestLinkMeetingId: UUID? = null,
        val linkMeetingId: UUID? = null,
        val status: MeetingStatusEnum = MeetingStatusEnum.DRAFT
)
