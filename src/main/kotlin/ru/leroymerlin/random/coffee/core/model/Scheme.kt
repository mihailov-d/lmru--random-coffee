package ru.leroymerlin.random.coffee.core.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum
import ru.leroymerlin.random.coffee.core.dto.UserStatusEnum
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId
import java.util.UUID


@Document data class User(
    @Id val id: UUID,
    val telegramUserId: String,
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
    val userId: UserId,
    val chatId: ChatId,
    val currentChatState: ChatState = ChatState.NONE,
    val draftBasicUser: UserBasicUpdateRequest? = null,
    val draftMeeting: MeetingUpdateRequest? = null,
    val draftCommunicationUser: UserCommunicationsUpdateRequest? = null,
    val draftAboutUser: UserAboutUpdateRequest? = null
)



//Mb to postgres
//@Document data class User(
//    @Id val id: String,
//    val firstName:  String,
//    val surName:    String,
//    val job:    Job? = null,
//    val mainTypeCommunication: UserPreferCommunicationEnum? = null,
//    val communication:  Set<Communication>? = null,
//    val meeting: Set<Meeting>? = null,
//    val session: Session? = null,
//    val status: UserStatusEnum? = null
//    )
//
//@Document data class Job(
//    @Id val id: String,
//    val companyName: String,
//    val companyPosition: String
//    )
//
//@Document data class Meeting(
//    @Id val id: String,
//    val about:  String,
//    val date:   LocalDate,
//    val name:   String,
//    val address:    String
//)
//
//@Document data class Communication(
//    @Id val pkCommunication: PKCommunication,
//    val login:  String
//)
//
//data class PKCommunication(
//    val id: String,
//    val type:  UserPreferCommunicationEnum
//)
//
//@Document data class Session(
//    @Id val id: String,
//    val sessionId: UUID,
//    val telegramUserId: String,
//    val chatState:  String
//)


