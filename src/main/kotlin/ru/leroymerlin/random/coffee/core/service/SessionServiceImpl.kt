package ru.leroymerlin.random.coffee.core.service

import org.springframework.stereotype.Component
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.SessionDto
import ru.leroymerlin.random.coffee.core.exception.SessionNotFoundException
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionServiceImpl : SessionService {
    val storage = ConcurrentHashMap<UserId, SessionDto>()
    val chatIdToUserIdStorage = ConcurrentHashMap<ChatId, UserId>()

    override fun getState(userId: UserId): SessionDto? {
        return storage[userId]
    }

    override fun saveState(userSessionState: SessionDto): SessionDto {
        storage[userSessionState.userId] = userSessionState
        chatIdToUserIdStorage[userSessionState.chatId] = userSessionState.userId
        return userSessionState
    }

    override fun getStateByChatId(chatId: ChatId): SessionDto {
        return chatIdToUserIdStorage[chatId]?.let { storage[it] } ?: throw SessionNotFoundException(chatId)
    }

    override fun updateChatStateByChatId(chatId: ChatId, chatState: ChatState) {
        getStateByChatId(chatId).apply {
            val updatedState = this.copy(currentChatState = chatState)
            saveState(updatedState)
            println("Current state - $updatedState")
        }
    }

    override fun getChatStateByChatId(chatId: ChatId): ChatState? {
        return getStateByChatId(chatId).currentChatState
    }
}
