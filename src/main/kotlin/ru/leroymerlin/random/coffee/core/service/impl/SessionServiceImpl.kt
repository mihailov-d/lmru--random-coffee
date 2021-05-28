package ru.leroymerlin.random.coffee.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.SessionDto
import ru.leroymerlin.random.coffee.core.exception.SessionNotFoundException
import ru.leroymerlin.random.coffee.core.model.Session
import ru.leroymerlin.random.coffee.core.repository.SessionRepository
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.util.TgChatId
import ru.leroymerlin.random.coffee.core.util.TgUserId
import java.time.LocalDateTime
import java.util.UUID

@Service
class SessionServiceImpl(
        private val sessionRepository: SessionRepository
) : SessionService {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun getState(tgUserId: TgUserId): SessionDto? {
        return try {
            modelTo(sessionRepository.findOneByTgUserId(tgUserId))
        } catch (ex: Exception) {
            logger.error("Cannot find session by userId", ex)
            null
        }
    }

    override fun saveState(userSessionState: SessionDto): SessionDto {
        val updatedSession = dtoTo(userSessionState)
        val oldSession = try {
            sessionRepository.findOneById(userSessionState.id)
        } catch (ex: Exception) {
            null
        }
        val savedSession = sessionRepository.save(
                if (oldSession != null) {
                    logger.debug("Save session")
                    updatedSession.copy(createdDate = oldSession.createdDate)
                } else {
                    logger.debug("Create session")
                    updatedSession
                }
        )
        return modelTo(savedSession)
    }

    override fun getStateByChatId(tgChatId: TgChatId): SessionDto {
        return try {
            modelTo(sessionRepository.findOneByTgChatId(tgChatId))
        } catch (ex: EmptyResultDataAccessException) {
            return saveState(SessionDto(
                    id = UUID.randomUUID(),
                    userId = UUID.randomUUID(),
                    telegramUserId = 0L,
                    telegramChatId = 0L,
                    currentChatState = ChatState.NONE
            ))
        } catch (ex: Exception) {
            logger.debug("Cannot find session by chatId")
            throw SessionNotFoundException(tgChatId)
        }
    }

    override fun updateChatStateByChatId(tgChatId: TgChatId, chatState: ChatState) {
        val session = sessionRepository.findOneByTgChatId(tgChatId)
        val updateSession = session.copy(currentChatState = chatState, editedDate = LocalDateTime.now())
        sessionRepository.save(updateSession)
        logger.debug("Update state for session by chatId")
    }

    override fun getChatStateByChatId(tgChatId: TgChatId): ChatState? {
        return try {
            sessionRepository.findOneByTgChatId(tgChatId)
        } catch (ex: Exception) {
            null
        }?.currentChatState
    }


    private fun modelTo(session: Session): SessionDto {
        return with(session) {
            SessionDto(
                    id,
                    userId,
                    tgUserId,
                    tgChatId,
                    currentChatState,
                    draftBasicUser,
                    draftMeeting,
                    draftCommunicationUser,
                    draftAboutUser
            )
        }
    }

    private fun dtoTo(sessionDto: SessionDto): Session {
        return with(sessionDto) {
            Session(
                    id,
                    userId,
                    telegramUserId,
                    telegramChatId,
                    currentChatState,
                    draftBasicUser,
                    draftMeeting,
                    draftCommunicationUser,
                    draftAboutUser
            )
        }
    }
}
