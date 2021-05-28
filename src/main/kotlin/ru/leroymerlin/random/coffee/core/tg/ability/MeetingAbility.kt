package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.TopicTypeEnum
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.service.UserService
import ru.leroymerlin.random.coffee.core.util.chatId
import ru.leroymerlin.random.coffee.core.util.keyboardRow
import ru.leroymerlin.random.coffee.core.util.stringChatId
import ru.leroymerlin.random.coffee.core.util.textEquals
import java.util.UUID
import java.util.function.Predicate

@Component
class MeetingAbility : AbilityExtension {
    @Autowired
    lateinit var sessionService: SessionService

    @Autowired
    lateinit var userService: UserService

    fun createMeeting(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.keyboard = listOf(
                keyboardRow(KeyboardButton.builder().text("О работе").build()),
                keyboardRow(KeyboardButton.builder().text("Давай отдахнем, не о работе").build())
        )
        message.replyMarkup = replyKeyboardMarkup
        message.chatId = update.stringChatId()
        message.text = "О чем хотите поговорить?"
        b.execute(message)
        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_MEETING_TOPIC_TYPE)
    }, textEquals("Создать встречу"))


    fun topicAboutReply(): Reply = Reply.of({ b, update ->
        val chatId = update.chatId()
        val messageText = update.message.text
        val topicType = when (messageText) {
            "О работе" -> TopicTypeEnum.ABOUT_WORK
            "Давай отдахнем, не о работе" -> TopicTypeEnum.ABOUT_OTHER
            else -> throw IllegalStateException("unknown message topicType: $messageText")
        }
        val userSession = sessionService.getStateByChatId(chatId).let {
            val updatedSession = it.copy(draftMeeting = it.draftMeeting?.copy(topicType = topicType)
                    ?: MeetingUpdateRequest(UUID.randomUUID(), null, null, topicType, null, ""))
            sessionService.saveState(updatedSession)
        }

        val message2 = SendMessage()
        val replyKeyboardMarkup = ReplyKeyboardMarkup()

        val currentState = sessionService.getStateByChatId(chatId)
        if (currentState.isMeetingFill()) {
            // TODO next step
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text("сегодня").build()),
                    keyboardRow(KeyboardButton.builder().text("завтра").build())
            )
            userService.update(userSession.draftBasicUser!!)
        } else {
            // TODO fill dates
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text("сегодня").build()),
                    keyboardRow(KeyboardButton.builder().text("завтра").build())
            )
        }
        message2.replyMarkup = replyKeyboardMarkup
        message2.chatId = update.stringChatId()
        message2.text = "Веберите дату встречи"
        b.execute(message2)
        sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
    }, textEquals("О работе").or(textEquals("Давай отдахнем, не о работе")))

    fun someTextReply(): Reply = Reply.of({ b, update ->
        val chatId = update.chatId()
        when (sessionService.getChatStateByChatId(chatId)) {
            ChatState.INPUT_MEETING_TOPIC_TYPE -> {
                println("some")
            }
            else -> {
                val message = SendMessage()
                message.replyMarkup = ReplyKeyboardRemove(true)
                message.chatId = update.stringChatId()
                message.text = "Неизвестное состояние. Сбрасываю диалог"
                b.execute(message)
                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
        }
    }, Predicate { update ->
        false
//        setOf("О работе", "Давай отдахнем, не о работе").contains(update.message.text).not() &&
//                sessionService.getStateByChatId(update.chatId()).let {
//                    setOf(ChatState.INPUT_MEETING_TOPIC_TYPE).contains(it.currentChatState)
//                }
    })
}
