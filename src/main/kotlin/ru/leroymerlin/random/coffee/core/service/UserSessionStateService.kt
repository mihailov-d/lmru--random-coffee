package ru.leroymerlin.random.coffee.core.service

import org.springframework.stereotype.Component
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.UserSessionState
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId
import java.util.concurrent.ConcurrentHashMap

@Component
class UserSessionStateService {
    val storage = ConcurrentHashMap<UserId, UserSessionState>()
    val chatIdToUserIdStorage = ConcurrentHashMap<ChatId, UserId>()

    fun getState(userId: UserId): UserSessionState? {
        return storage[userId]
    }

    fun saveState(userSessionState: UserSessionState) {
        storage[userSessionState.userId] = userSessionState
        chatIdToUserIdStorage[userSessionState.chatId] = userSessionState.userId
    }

    fun getStateByChatId(chatId: ChatId): UserSessionState? {
        return chatIdToUserIdStorage[chatId]?.let { storage[it] }
    }

    fun updateChatStateByChatId(chatId: ChatId, chatState: ChatState) {
        getStateByChatId(chatId)?.apply {
            saveState(this.copy(currentChatState = chatState))
        }
    }

    fun getChatStateByChatId(chatId: ChatId): ChatState? {
        return getStateByChatId(chatId)?.currentChatState
    }
}
