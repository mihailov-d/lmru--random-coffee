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
import java.time.LocalDate
import java.time.ZoneId
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

        if (userSession.isMeetingFill()) {
            // TODO next step
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text("Опубликовать анкету для встречи").build())
            )
            // TODO save draft meeting
//            userService.update(userSession.draftMeeting!!)
            message2.text = "Спасибо, публикуем анкету для встречи?"
            sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
        } else {
            // TODO fill dates
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text("Сегодня").build()),
                    keyboardRow(KeyboardButton.builder().text("Завтра").build()),
                    keyboardRow(KeyboardButton.builder().text("Послезавтра").build())
            )
            message2.text = "Веберите дату встречи"
            sessionService.updateChatStateByChatId(chatId, ChatState.INPUT_MEETING_DATE)
        }
        message2.replyMarkup = replyKeyboardMarkup
        message2.chatId = update.stringChatId()
        b.execute(message2)
    }, textEquals("О работе").or(textEquals("Давай отдахнем, не о работе")))

    fun meetingDateReply(): Reply = Reply.of({ d, update ->
        val text = update.message.text
        val chatId = update.chatId()

        val moscowZoneId = ZoneId.of("Europe/Moscow")
        val preferDate = when (text) {
            "Сегодня" -> LocalDate.now(moscowZoneId)
            "Завтра" -> LocalDate.now(moscowZoneId).plusDays(1)
            "Послезавтра" -> LocalDate.now(moscowZoneId).plusDays(2)
            else -> throw IllegalStateException("unknown meeting date: $text")
        }

        val userSession = sessionService.getStateByChatId(chatId).let {
            val updatedSession = it.copy(draftMeeting = it.draftMeeting?.copy(preferDate = preferDate)
                    ?: MeetingUpdateRequest(UUID.randomUUID(), null, null, null, preferDate, null, ""))
            sessionService.saveState(updatedSession)
        }
        sessionService.updateChatStateByChatId(chatId, ChatState.NONE)

        val message = SendMessage()
        message.chatId = update.stringChatId()
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        if (userSession.isMeetingFill()) {
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text("Опубликовать анкету для встречи").build())
            )
            message.replyMarkup = replyKeyboardMarkup
            message.text = "Спасибо, публикуем анкету для встречи?"
            sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
        } else {
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text("О работе").build()),
                    keyboardRow(KeyboardButton.builder().text("Давай отдахнем, не о работе").build())
            )
            message.replyMarkup = replyKeyboardMarkup
            message.text = "О чем хотите поговорить?"
            sessionService.updateChatStateByChatId(chatId, ChatState.INPUT_MEETING_TOPIC_TYPE)
        }
        d.execute(message)
    }, Predicate { update -> sessionService.getChatStateByChatId(update.chatId()) == ChatState.INPUT_MEETING_DATE })

    fun someTextReply(): Reply = Reply.of({ b, update ->
        val chatId = update.chatId()
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Неизвестное состояние. Сбрасываю диалог"
        b.execute(message)
        sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
    }, Predicate { update ->
        false
//        setOf("О работе", "Давай отдахнем, не о работе").contains(update.message.text).not() &&
//                sessionService.getStateByChatId(update.chatId()).let {
//                    setOf(ChatState.INPUT_MEETING_TOPIC_TYPE).contains(it.currentChatState)
//                }
    })
}
