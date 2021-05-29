package ru.leroymerlin.random.coffee.core.dto

import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.util.TgChatId
import ru.leroymerlin.random.coffee.core.util.TgUserId
import java.util.UUID

// future @Document
data class SessionDto(
        // internal id
        val id: UUID,
        // internal userId
        val userId: UUID,
        val telegramUserId: TgUserId,
        val telegramChatId: TgChatId,
        val currentChatState: ChatState = ChatState.NONE,
        val draftBasicUser: UserBasicUpdateRequest? = null,
        val draftMeeting: MeetingUpdateRequest? = null,
        val draftCommunicationUser: UserCommunicationsUpdateRequest? = null,
        val draftAboutUser: UserAboutUpdateRequest? = null
) {
    fun isNameAndSurnameFill(): Boolean = draftBasicUser?.let {
        return it.name.isNullOrBlank().not() && it.surname.isNullOrBlank().not()
    } ?: false

    fun isAboutFill(): Boolean = draftAboutUser?.let {
        return it.aboutJob.isNullOrBlank().not() && it.aboutMe.isNullOrBlank().not()
    } ?: false

    fun isCommunicationFill(): Boolean = draftCommunicationUser?.let { communicationRequest ->
        return communicationRequest.preferCommunications != null && communicationRequest.preferCommunications.isNotEmpty()
                && communicationRequest.preferCommunications
                .first().let {
                    when (it) {
                        UserPreferCommunicationEnum.EMAIL -> communicationRequest.email.isNullOrBlank().not()
                        UserPreferCommunicationEnum.PHONE -> communicationRequest.phone.isNullOrBlank().not()
                        UserPreferCommunicationEnum.TELEGRAM -> communicationRequest.telegramUsername.isNullOrBlank().not()
                        else -> {
                            println("Invalid 'UserPreferCommunicationEnum' $it")
                            return false
                        }
                    }
                }
    } ?: false

    fun isMeetingFill(): Boolean = draftMeeting?.let {
        return it.topicType != null && it.preferDate != null
    } ?: false

    fun isProfileFill(): Boolean = isAboutFill() && isCommunicationFill() && isNameAndSurnameFill()
}
