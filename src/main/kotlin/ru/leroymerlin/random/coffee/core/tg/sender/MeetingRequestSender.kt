package ru.leroymerlin.random.coffee.core.tg.sender

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.leroymerlin.random.coffee.configuration.RandomCoffeeBot
import ru.leroymerlin.random.coffee.core.dto.UserDto
import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum.EMAIL
import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum.PHONE
import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum.TELEGRAM
import ru.leroymerlin.random.coffee.core.model.User
import ru.leroymerlin.random.coffee.core.service.MeetingService
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.service.UserService
import ru.leroymerlin.random.coffee.core.util.TgChatId
import ru.leroymerlin.random.coffee.core.util.message.MessageUtil.meetingTopicMessageString
import java.util.UUID

@Component
class MeetingRequestSender {

    @Autowired
    lateinit var randomCoffeeBot: RandomCoffeeBot

    @Autowired
    lateinit var meetingService: MeetingService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var sessionService: SessionService

    fun sendPropose(chatId: TgChatId, meetingId: UUID) {
        val meeting = meetingService.get(meetingId)

        val userCreatedMeeting = userService.get(meeting.userId)

        val message = SendMessage()
        message.chatId = chatId.toString()

        message.text = """
            Предложение о встрече c "${userCreatedMeeting.name} ${userCreatedMeeting.surname}", ${meeting.preferDate.toLocalDate()}
            Анкета 📑
            Обо мне:
            ${userCreatedMeeting.aboutMe}
            О работе:
            ${userCreatedMeeting.aboutJob}

            Тема для встречи: '${meetingTopicMessageString(meeting.topicTypeEnum)}'
        """.trimIndent()
//        message.enableMarkdown(true)


        val inlineKeyboardMarkup = InlineKeyboardMarkup(
                listOf(listOf(
                        InlineKeyboardButton.builder()
                                .text("Отлично, подтверждаю")
                                .callbackData("mr__${meetingId}__approve")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Не смогу")
                                .callbackData("mr__${meetingId}__nope")
                                .build()
                ))
        )
        message.replyMarkup = inlineKeyboardMarkup

        randomCoffeeBot.execute(message)
    }

    fun sendSuccess(meetingId: UUID) {
        val meeting = meetingService.get(meetingId)

        val userCreatedMeeting = userService.get(meeting.userId)

        val session = sessionService.getState(userCreatedMeeting.telegramUserId!!)!!

        val messageToMeetingCreator = SendMessage()
        messageToMeetingCreator.chatId = session.telegramChatId.toString()
        messageToMeetingCreator.text = """
            Предложение о встрече на ${meeting.preferDate.toLocalDate()} подтверждено
            
            Свяжитесь друг с другом по указанным контактным данным:
            ${getContact(userCreatedMeeting)}
        """.trimIndent()
        messageToMeetingCreator.enableMarkdown(true)
        randomCoffeeBot.execute(messageToMeetingCreator)
    }

    fun cancelMeetingMessage(meetingId: UUID, targetChatId: TgChatId) {
        val meeting = meetingService.get(meetingId)
        // TODO logic with who meeting end

        val targetSession = sessionService.getStateByChatId(targetChatId)
        val targetUser = userService.get(targetSession.userId)
        val meetingCreatorUser = userService.get(meeting.userId)
        val meetingCreatorSession = sessionService.getState(meetingCreatorUser.telegramUserId!!)!!

        val userAddressat = if (meetingCreatorSession.telegramChatId == targetChatId) {
            targetUser
        } else {
            meetingCreatorUser
        }

        val messageToMeetingCreator = SendMessage()
        messageToMeetingCreator.chatId = targetChatId.toString()
        messageToMeetingCreator.text = """
            Встреча с пользователем "${userAddressat.name} ${userAddressat.surname}" на ${meeting.preferDate.toLocalDate()} отменена.
        """.trimIndent()
//        messageToMeetingCreator.enableMarkdown(true)
        randomCoffeeBot.execute(messageToMeetingCreator)
    }

    fun getContact(user: User): String {
        return when (user.preferCommunications!!.first()) {
            TELEGRAM -> "Telegram @${user.telegramUsername}"
            PHONE -> "телефон ${user.phone}"
            EMAIL -> "email ${user.email}"
        }
    }
}
