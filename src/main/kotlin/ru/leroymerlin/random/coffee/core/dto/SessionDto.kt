package ru.leroymerlin.random.coffee.core.dto

import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId
import java.util.UUID

// future @Document
data class SessionDto(
        // internal id
        val id: UUID,
        val userId: UserId,
        val chatId: ChatId,
        val currentChatState: ChatState = ChatState.NONE,
        val draftBasicUser: UserBasicUpdateRequest? = null,
        val draftMeeting: MeetingUpdateRequest? = null,
        val draftCommunicationUser: UserCommunicationsUpdateRequest? = null,
        val draftAboutUser: UserAboutUpdateRequest? = null
) {
    fun isNameAndSurnameFill(): Boolean {
        return draftBasicUser?.let {
            return it.name.isNullOrBlank().not() && it.surname.isNullOrBlank().not()
        } ?: false
    }

    fun isAboutFill(): Boolean = draftAboutUser?.let {
        return it.aboutJob.isNullOrBlank().not() && it.aboutMe.isNullOrBlank().not()
    } ?: false
}
