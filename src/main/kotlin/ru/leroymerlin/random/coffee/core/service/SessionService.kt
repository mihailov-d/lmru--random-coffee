package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.SessionDto
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId

interface SessionService {
    // Create session
    // Get session by id (channelId:userId)
    // Save session
    fun getState(userId: UserId): SessionDto?

    fun saveState(userSessionState: SessionDto): SessionDto

    fun getStateByChatId(chatId: ChatId): SessionDto

    fun updateChatStateByChatId(chatId: ChatId, chatState: ChatState)

    fun getChatStateByChatId(chatId: ChatId): ChatState?
}
