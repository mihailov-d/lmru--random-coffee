package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.request.MeetingUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.TopicTypeEnum
import ru.leroymerlin.random.coffee.core.service.MeetingService
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
    lateinit var meetingService: MeetingService

    @Autowired
    lateinit var userService: UserService

    fun createCoffeeReqAbility(): Ability = Ability.builder()
            .name("create_coffee_request")
            .info("create Coffee Req")
            .privacy(Privacy.PUBLIC)
            .locality(Locality.USER)
            .input(0)
            .action { ctx: MessageContext ->
                val sessionDto = sessionService.getStateByChatId(ctx.update().chatId())
                sessionService.saveState(sessionDto.copy(draftMeeting = null, currentChatState = ChatState.NONE))

                val message = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()
                replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_ABOUT_WORK.command).build()),
                        keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_ABOUT_SOMETHING.command).build())
                )
                message.replyMarkup = replyKeyboardMarkup
                message.chatId = ctx.update().stringChatId()
                message.text = "О чем хочешь поговорить за кофе?"
                ctx.bot().execute(message)
                sessionService.updateChatStateByChatId(ctx.update().chatId(), ChatState.INPUT_MEETING_TOPIC_TYPE)
            }
            .post { ctx: MessageContext -> println("post ${ctx.arguments()}") }
            .build()

    fun getListMeeting(): Ability = Ability.builder()
        .name("meeting_list")
        .info("Get All active")
        .privacy(Privacy.PUBLIC)
        .locality(Locality.USER)
        .input(0)
        .action { ctx: MessageContext ->
            val sessionDto = sessionService.getStateByChatId(ctx.update().chatId())
            val meetingSet = meetingService.getAllActiveMeetingByUser(sessionDto.userId)
            if (meetingSet.isEmpty()) {
                val message = SendMessage()
                message.chatId = ctx.update().stringChatId()
                message.text = "У вас нет активных встреч"
                ctx.bot().execute(message)
            }
            meetingSet.forEach { meeting ->
                val inlineKeyboardMarkup = InlineKeyboardMarkup()
                inlineKeyboardMarkup.keyboard = listOf(
                    listOf(
                        InlineKeyboardButton.builder().callbackData("meeting_end=" + meeting.id.toString())
                            .text("Завершить встречу").build(),
                        InlineKeyboardButton.builder().callbackData("meeting_cancel=" + meeting.id.toString())
                            .text("Отменить встречу").build()
                    )
                )
                //set user id with who meeting
                val user = userService.getUserById(meeting.userId)
                val message = SendMessage()
                message.replyMarkup = inlineKeyboardMarkup
                message.chatId = ctx.update().stringChatId()
                message.text =
                    "Встреча с : " + user.telegramUsername + "\n" + "Время встречи: " + meeting.preferDate + "\n" + "Тема встречи: " + meeting.topicTypeEnum.name

                ctx.bot().execute(message)
            }
        }
        .post { ctx: MessageContext -> println("post ${ctx.arguments()}") }
        .build()

    fun createMeeting(): Reply = Reply.of({ b, update ->
        sessionService.getStateByChatId(update.chatId()).apply {
            sessionService.saveState(this.copy(
                    draftMeeting = null,
                    currentChatState = ChatState.NONE
            ))
        }

        val message = SendMessage()
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.keyboard = listOf(
                keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_ABOUT_WORK.command).build()),
                keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_ABOUT_SOMETHING.command).build())
        )
        message.replyMarkup = replyKeyboardMarkup
        message.chatId = update.stringChatId()
        message.text = "О чем хочешь поговорить за кофе?"
        b.execute(message)
        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_MEETING_TOPIC_TYPE)
    }, textEquals(CommandList.MEETING_CREATE.command).or(textEquals(CommandList.MEETING_CREATE_FROM_START.command)))

    fun getListMeetingWithAction(): Reply = Reply.of({ b, update ->
        val sessionDto = sessionService.getStateByChatId(update.chatId())
        val meetingSet = meetingService.getAllActiveMeetingByUser(sessionDto.userId)
        if (meetingSet.isEmpty()) {
            val message = SendMessage()
            message.chatId = update.stringChatId()
            message.text = "У вас нет активных встреч"
            b.execute(message)
        }
        meetingSet.forEach { meeting ->
            val inlineKeyboardMarkup = InlineKeyboardMarkup()
            inlineKeyboardMarkup.keyboard = listOf(
                listOf(
                    InlineKeyboardButton.builder().callbackData("meeting_end=" + meeting.id.toString()).text("Завершить встречу").build(),
                    InlineKeyboardButton.builder().callbackData("meeting_cancel=" + meeting.id.toString()).text("Отменить встречу").build()
                )
            )
            //set user id with who meeting
            val user = userService.getUserById(meeting.userId)
            val message = SendMessage()
            message.replyMarkup = inlineKeyboardMarkup
            message.chatId = update.stringChatId()
            message.text = "Встреча с : " + user.telegramUsername + "\n" + "Время встречи: " + meeting.preferDate + "\n" + "Тема встречи: " + meeting.topicTypeEnum.name

            b.execute(message)
        }
    }, textEquals(CommandList.MEETING_LIST.command))

    fun endMeeting(): Reply = Reply.of({ b, update ->
        val sessionDto = sessionService.getStateByChatId(update.chatId())
        val callbackMap = update.callbackQuery.data.split(",").associate {
            val (left, right) = it.split("=")
            left to UUID.fromString(right)
        }
        callbackMap["meeting_end"]?.let { meetingService.end(it) }
        val message = SendMessage()
        message.chatId = update.stringChatId()
        message.text = "Встреча закончена, надеюсь тебе понравилось :)"
        b.execute(message)
    }, Predicate { update -> update.hasCallbackQuery() && update.callbackQuery.data.contains("meeting_end") })

    fun cancelMeeting(): Reply = Reply.of({ b, update ->
        val sessionDto = sessionService.getStateByChatId(update.chatId())
        val callbackMap = update.callbackQuery.data.split(",").associate {
            val (left, right) = it.split("=")
            left to UUID.fromString(right)
        }
        callbackMap["meeting_cancel"]?.let { meetingService.cancel(it) }
        val message = SendMessage()
        message.chatId = update.stringChatId()
        message.text = "Вы отменили встречу, печаль :("
        b.execute(message)
    }, Predicate { update -> update.hasCallbackQuery() && update.callbackQuery.data.contains("meeting_cancel") })

    fun topicAboutReply(): Reply = Reply.of({ b, update ->
        val chatId = update.chatId()
        val messageText = update.message.text
        val topicType = when (messageText) {
            CommandList.MEETING_ABOUT_WORK.command -> TopicTypeEnum.ABOUT_WORK
            CommandList.MEETING_ABOUT_SOMETHING.command -> TopicTypeEnum.ABOUT_OTHER
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
                    keyboardRow(KeyboardButton.builder().text(CommandList.MATCH_MEETING_START.command).build())
            )
            // TODO save draft meeting
//            userService.update(userSession.draftMeeting!!)
            message2.text = "Спасибо, публикуем анкету для встречи?"
            sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
        } else {
            // TODO fill dates
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_DATE_TODAY.command).build()),
                    keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_DATE_TOMORROW.command).build()),
                    keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_DATE_AFTER_TOMORROW.command).build())
            )
            message2.text = "Выбрать день"
            sessionService.updateChatStateByChatId(chatId, ChatState.INPUT_MEETING_DATE)
        }
        message2.replyMarkup = replyKeyboardMarkup
        message2.chatId = update.stringChatId()
        b.execute(message2)
    }, textEquals(CommandList.MEETING_ABOUT_WORK.command).or(textEquals(CommandList.MEETING_ABOUT_SOMETHING.command)))

    fun meetingDateReply(): Reply = Reply.of({ d, update ->
        val text = update.message.text
        val chatId = update.chatId()

        val moscowZoneId = ZoneId.of("Europe/Moscow")
        val preferDate = when (text) {
            CommandList.MEETING_DATE_TODAY.command -> LocalDate.now(moscowZoneId)
            CommandList.MEETING_DATE_TOMORROW.command -> LocalDate.now(moscowZoneId).plusDays(1)
            CommandList.MEETING_DATE_AFTER_TOMORROW.command -> LocalDate.now(moscowZoneId).plusDays(2)
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
                    keyboardRow(KeyboardButton.builder().text(CommandList.MATCH_MEETING_START.command).build())
            )
            message.replyMarkup = replyKeyboardMarkup
            message.text = "Спасибо, публикуем анкету для встречи?"
            sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
        } else {
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_ABOUT_WORK.command).build()),
                    keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_ABOUT_SOMETHING.command).build())
            )
            message.replyMarkup = replyKeyboardMarkup
            message.text = "О чем хочешь поговорить за кофе?"
            sessionService.updateChatStateByChatId(chatId, ChatState.INPUT_MEETING_TOPIC_TYPE)
        }
        d.execute(message)
    }, Predicate { update ->
        update.hasMessage() &&
                update.message.hasText() &&
                setOf(CommandList.MEETING_DATE_TODAY.command, CommandList.MEETING_DATE_TOMORROW.command, CommandList.MEETING_DATE_AFTER_TOMORROW.command).contains(update.message.text) &&
                sessionService.getChatStateByChatId(update.chatId()) == ChatState.INPUT_MEETING_DATE
    })

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
//        setOf(CommandList.MEETING_ABOUT_WORK.command, CommandList.MEETING_ABOUT_SOMETHING.command).contains(update.message.text).not() &&
//                sessionService.getStateByChatId(update.chatId()).let {
//                    setOf(ChatState.INPUT_MEETING_TOPIC_TYPE).contains(it.currentChatState)
//                }
    })
}
