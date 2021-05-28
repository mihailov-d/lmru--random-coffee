package ru.leroymerlin.random.coffee.core.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.UserStatusEnum
import ru.leroymerlin.random.coffee.core.model.Meeting
import ru.leroymerlin.random.coffee.core.model.Session
import ru.leroymerlin.random.coffee.core.model.User
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId
import java.util.UUID

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findOneById(id: UUID): User
    fun findOneByTelegramUserId(telegramUserId: UserId): User
}

@Repository
interface MeetingRepository : MongoRepository<Meeting, String> {
    fun findOneById(id: UUID): Meeting
    fun findAllByUserIdAndStatus(userId: UUID, status: MeetingStatusEnum): List<Meeting>
}

@Repository
interface SessionRepository : MongoRepository<Session, String> {
    fun findOneById(id: UUID): Session
    fun findOneByChatId(chatId: ChatId): Session
    fun findOneByUserId(userId: UserId): Session
    fun findOneByCurrentChatStateAndChatId(chatState: ChatState, chatId: ChatId): Session
}




