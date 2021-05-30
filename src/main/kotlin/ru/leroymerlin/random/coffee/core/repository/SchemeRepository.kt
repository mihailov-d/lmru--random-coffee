package ru.leroymerlin.random.coffee.core.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum
import ru.leroymerlin.random.coffee.core.dto.request.TopicTypeEnum
import ru.leroymerlin.random.coffee.core.model.Meeting
import ru.leroymerlin.random.coffee.core.model.Session
import ru.leroymerlin.random.coffee.core.model.User
import ru.leroymerlin.random.coffee.core.util.TgChatId
import ru.leroymerlin.random.coffee.core.util.TgUserId
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findOneById(id: UUID): User
    fun deleteById(id: UUID)
    fun findByTelegramUserId(telegramUserId: TgUserId): User
}

@Repository
interface MeetingRepository : MongoRepository<Meeting, String> {
    fun findOneById(id: UUID): Meeting
    fun findAllByUserIdAndStatus(userId: UUID, status: MeetingStatusEnum): Set<Meeting>
    fun deleteById(id: UUID)
    fun findAllByStatusAndTopicTypeEnumAndPreferDateBetween(
        status: MeetingStatusEnum,
        topicTypeEnum: TopicTypeEnum,
        preferDateStart: LocalDateTime,
        preferDateEnd: LocalDateTime
    ): List<Meeting>
    fun findAllByStatusEquals(status: MeetingStatusEnum): List<Meeting>
}

@Repository
interface SessionRepository : MongoRepository<Session, String> {
    fun findOneById(id: UUID): Session
    fun findOneByTgChatId(tgChatId: TgChatId): Session
    fun findOneByTgUserId(tgUserId: TgUserId): Session
    fun findOneByCurrentChatStateAndTgChatId(chatState: ChatState, tgChatId: TgChatId): Session
}




