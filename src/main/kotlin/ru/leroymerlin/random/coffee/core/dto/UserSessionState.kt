package ru.leroymerlin.random.coffee.core.dto

import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId

data class UserSessionState(
        val userId: UserId,
        val chatId: ChatId,
        val currentChatState: ChatState
)

enum class ChatState {
    INPUT_EMAIL,
    INPUT_PHONE,
    NONE
}
