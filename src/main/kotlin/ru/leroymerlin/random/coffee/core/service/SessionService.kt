package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.SessionDto
import ru.leroymerlin.random.coffee.core.util.TgChatId
import ru.leroymerlin.random.coffee.core.util.TgUserId

interface SessionService {
    // Create session
    // Get session by id (channelId:userId)
    // Save session
    fun getState(tgUserId: TgUserId): SessionDto?

    fun saveState(userSessionState: SessionDto): SessionDto

    fun getStateByChatId(tgChatId: TgChatId): SessionDto

    fun updateChatStateByChatId(tgChatId: TgChatId, chatState: ChatState)

    fun getChatStateByChatId(tgChatId: TgChatId): ChatState?
}
