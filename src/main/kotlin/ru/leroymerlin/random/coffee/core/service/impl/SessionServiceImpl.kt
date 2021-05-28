package ru.leroymerlin.random.coffee.core.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.SessionDto
import ru.leroymerlin.random.coffee.core.exception.SessionNotFoundException
import ru.leroymerlin.random.coffee.core.model.Session
import ru.leroymerlin.random.coffee.core.repository.SessionRepository
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.util.ChatId
import ru.leroymerlin.random.coffee.core.util.UserId
import java.time.LocalDateTime

@Service
class SessionServiceImpl(
        private val sessionRepository: SessionRepository
) : SessionService {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun getState(userId: UserId): SessionDto? {
        return try {
            modelTo(sessionRepository.findOneByUserId(userId))
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
            throw SessionNotFoundException(userSessionState.chatId)
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

    override fun getStateByChatId(chatId: ChatId): SessionDto {
        return try {
            modelTo(sessionRepository.findOneByChatId(chatId))
        } catch (ex: Exception) {
            logger.debug("Cannot find session by chatId")
            throw SessionNotFoundException(chatId)
        }
    }

    override fun updateChatStateByChatId(chatId: ChatId, chatState: ChatState) {
        val session = sessionRepository.findOneByChatId(chatId)
        val updateSession = session.copy(currentChatState = chatState, editedDate = LocalDateTime.now())
        sessionRepository.save(updateSession)
        logger.debug("Update state for session by chatId")
    }

    override fun getChatStateByChatId(chatId: ChatId): ChatState? {
        return try {
            sessionRepository.findOneByChatId(chatId)
        } catch (ex: Exception) {
            null
        }?.currentChatState
    }


    private fun modelTo(session: Session): SessionDto {
        return with(session) {
            SessionDto(
                    id,
                    userId,
                    chatId,
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
                    chatId,
                    currentChatState,
                    draftBasicUser,
                    draftMeeting,
                    draftCommunicationUser,
                    draftAboutUser
            )
        }
    }
}
