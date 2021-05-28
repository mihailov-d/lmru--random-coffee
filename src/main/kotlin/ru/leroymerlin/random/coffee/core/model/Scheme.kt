package ru.leroymerlin.random.coffee.core.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.LocationTypeEnum
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum
import ru.leroymerlin.random.coffee.core.dto.UserStatusEnum
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId
import java.time.LocalDateTime
import java.util.UUID


@Document data class User(
    @Id val id: UUID,
    val telegramUserId: UserId,
    val name: String? = null,
    val surname: String? = null,
    val preferCommunications: Set<UserPreferCommunicationEnum>? = null,
    val email: String? = null,
    val phone: String? = null,
    val aboutMe: String? = null,
    val aboutJob: String? = null,
    val status: UserStatusEnum = UserStatusEnum.DRAFT
)

@Document data class Session(
    @Id val id: UUID,
    val userId: UserId,
    val chatId: ChatId,
    val currentChatState: ChatState = ChatState.NONE,
    val draftBasicUser: UserBasicUpdateRequest? = null,
    val draftMeeting: MeetingUpdateRequest? = null,
    val draftCommunicationUser: UserCommunicationsUpdateRequest? = null,
    val draftAboutUser: UserAboutUpdateRequest? = null
)

@Document data class Meeting(
    @Id val id: UUID,
    val userId: UUID,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime,
    val aim: String? = null,
    val comment: String? = null,
    val locationType: LocationTypeEnum? = null,
    val location: String? = null,
    val status: MeetingStatusEnum = MeetingStatusEnum.DRAFT
)
