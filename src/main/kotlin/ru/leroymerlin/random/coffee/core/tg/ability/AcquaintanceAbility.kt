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
import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum
import ru.leroymerlin.random.coffee.core.dto.request.UserAboutUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserBasicUpdateRequest
import ru.leroymerlin.random.coffee.core.dto.request.UserCommunicationsUpdateRequest
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.service.UserService
import ru.leroymerlin.random.coffee.core.util.chatId
import ru.leroymerlin.random.coffee.core.util.keyboardRow
import ru.leroymerlin.random.coffee.core.util.stringChatId
import ru.leroymerlin.random.coffee.core.util.textEquals
import java.util.UUID
import java.util.function.Predicate

@Component
class AcquaintanceAbility : AbilityExtension {

    @Autowired
    lateinit var sessionService: SessionService

    @Autowired
    lateinit var userService: UserService

    fun fillCardReply(): Reply {
        return Reply.of({ b, update ->
            val message = SendMessage()
            val replyKeyboardMarkup = ReplyKeyboardMarkup()
            replyKeyboardMarkup.keyboard = listOf(
                    keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_EMAIL.command).build()),
                    keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_PHONE.command).build()),
                    keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_TELEGRAM.command).build())
            )
            message.replyMarkup = replyKeyboardMarkup
            message.chatId = update.stringChatId()
            message.text = "Выберите наиболее удобный связи с тобой"
            b.execute(message)
        }, textEquals("Заполнить карточку"))
    }

    fun typeEmailReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите вашу почту, по которой с вами можно связаться для встречи"
        b.execute(message)

        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_EMAIL)
    }, textEquals(CommandList.ACQUAINTANCE_INPUT_EMAIL.command))

    fun typePhoneReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите ваш телефон, по которому с вами можно связаться для встречи"
        b.execute(message)

        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_PHONE)
    }, textEquals(CommandList.ACQUAINTANCE_INPUT_PHONE.command))

    fun typeInputNameReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите ваше имя для анкеты"
        b.execute(message)

        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_NAME)
    }, textEquals(CommandList.ACQUAINTANCE_INPUT_NAME.command))

    fun typeInputSurnameReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите вашу фамилию для анкеты"
        b.execute(message)

        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_SURNAME)
    }, textEquals(CommandList.ACQUAINTANCE_INPUT_SURNAME.command))

    fun typeAboutMeReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите информацию о себе для анкеты"
        b.execute(message)

        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_ABOUT_ME)
    }, textEquals(CommandList.ACQUAINTANCE_INPUT_ABOUT_ME.command))

    fun typeAboutJobReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите информацию о работе для анкеты"
        b.execute(message)

        sessionService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_ABOUT_JOB)
    }, textEquals(CommandList.ACQUAINTANCE_INPUT_ABOUT_WORK.command))

    fun typeTelegramReply(): Reply = Reply.of({ b, update ->
        // TODO save telegram id as contact type
        val userName = update.message.chat.userName

        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        // IT THROW EXCEPTION - 400
//        message.enableMarkdown(true)
        message.text = "Спасибо, мы сохранили ваш telegram id как контактный: `@$userName`"
        b.execute(message)

        val userSession = sessionService.getStateByChatId(update.chatId()).let {
            val updatedSession = it.copy(
                    draftCommunicationUser = it.draftCommunicationUser?.copy(
                            telegram = userName,
                            preferCommunications = setOf(UserPreferCommunicationEnum.TELEGRAM)
                    )
                            ?: UserCommunicationsUpdateRequest(
                                    UUID.randomUUID(),
                                    null,
                                    null,
                                    userName,
                                    setOf(UserPreferCommunicationEnum.TELEGRAM)
                            )
            )
            sessionService.saveState(updatedSession)
        }
        sessionService.updateChatStateByChatId(update.chatId(), ChatState.NONE)
        userService.update(userSession.draftCommunicationUser!!)

        val message2 = SendMessage()
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.keyboard = listOf(
                keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_NAME.command).build()),
                keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_SURNAME.command).build())
        )
        message2.replyMarkup = replyKeyboardMarkup
        message2.chatId = update.stringChatId()
        message2.text = "Введите информацию о себе"
        b.execute(message2)
    }, textEquals(CommandList.ACQUAINTANCE_INPUT_TELEGRAM.command))

    fun commonTextAbility(): Reply = Reply.of({ b, update ->
        val chatId = update.chatId()

        when (sessionService.getChatStateByChatId(chatId)) {
            ChatState.INPUT_EMAIL -> {
                val userEmail = update.message.text.trim()
                // TODO email validation
                b.silent().sendMd("Спасибо, мы сохранили твой email: `$userEmail`", chatId)
                val userSession = sessionService.getStateByChatId(chatId).let {
                    val updatedSession = it.copy(
                            draftCommunicationUser = it.draftCommunicationUser?.copy(
                                    email = userEmail,
                                    preferCommunications = setOf(UserPreferCommunicationEnum.EMAIL)
                            )
                                    ?: UserCommunicationsUpdateRequest(
                                            UUID.randomUUID(),
                                            null,
                                            userEmail,
                                            null,
                                            setOf(UserPreferCommunicationEnum.EMAIL)
                                    )
                    )
                    sessionService.saveState(updatedSession)
                }
                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
                userService.update(userSession.draftCommunicationUser!!)
            }
            ChatState.INPUT_PHONE -> {
                val userPhone = update.message.text.trim()
                // TODO phone validation
                b.silent().sendMd("Спасибо, мы сохранили твой телефон: `$userPhone`", chatId)
                val userSession = sessionService.getStateByChatId(chatId).let {
                    val updatedSession = it.copy(
                            draftCommunicationUser = it.draftCommunicationUser?.copy(
                                    phone = userPhone,
                                    preferCommunications = setOf(UserPreferCommunicationEnum.PHONE)
                            )
                                    ?: UserCommunicationsUpdateRequest(
                                            UUID.randomUUID(),
                                            phone = userPhone,
                                            null,
                                            null,
                                            setOf(UserPreferCommunicationEnum.PHONE)
                                    )
                    )
                    sessionService.saveState(updatedSession)
                }

                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
                userService.update(userSession.draftCommunicationUser!!)
            }
            ChatState.INPUT_NAME -> {
                val name = update.message.text.trim()
                // TODO name validation

                val userSession = sessionService.getStateByChatId(chatId).let {
                    val updatedSession = it.copy(
                            draftBasicUser = it.draftBasicUser?.copy(name = name)
                                    ?: UserBasicUpdateRequest(UUID.randomUUID(), name, null)
                    )
                    sessionService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()
                val currentState = sessionService.getStateByChatId(chatId)
                if (currentState.isNameAndSurnameFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_ABOUT_ME.command).build()),
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_ABOUT_WORK.command).build())
                    )
                    userService.update(userSession.draftBasicUser!!)
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_SURNAME.command).build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваше имя для анкеты: $name"
                b.execute(message2)
                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_SURNAME -> {
                val surname = update.message.text.trim()
                // TODO surname validation

                val userSession = sessionService.getStateByChatId(chatId).let {
                    val updatedSession = it.copy(
                            draftBasicUser = it.draftBasicUser?.copy(surname = surname)
                                    ?: UserBasicUpdateRequest(UUID.randomUUID(), null, surname)
                    )
                    sessionService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()

                val currentState = sessionService.getStateByChatId(chatId)
                if (currentState.isNameAndSurnameFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_ABOUT_ME.command).build()),
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_ABOUT_WORK.command).build())
                    )
                    userService.update(userSession.draftBasicUser!!)
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_NAME.command).build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваша фамилия для анкеты: $surname"
                b.execute(message2)
                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_ABOUT_ME -> {
                val aboutMe = update.message.text.trim()

                val userSession = sessionService.getStateByChatId(chatId).let {
                    val updatedSession = it.copy(
                            draftAboutUser = it.draftAboutUser?.copy(aboutMe = aboutMe)
                                    ?: UserAboutUpdateRequest(UUID.randomUUID(), aboutMe, null)
                    )
                    sessionService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()

                val currentState = sessionService.getStateByChatId(chatId)
                if (currentState.isAboutFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_CREATE.command).build())
                    )
                    userService.update(userSession.draftAboutUser!!)
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_ABOUT_WORK.command).build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваша информация о себе в анкете обновлена"
                b.execute(message2)
                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_ABOUT_JOB -> {
                val aboutJob = update.message.text.trim()

                val userSession = sessionService.getStateByChatId(chatId).let {
                    val updatedSession = it.copy(
                            draftAboutUser = it.draftAboutUser?.copy(aboutJob = aboutJob)
                                    ?: UserAboutUpdateRequest(UUID.randomUUID(), null, aboutJob)
                    )
                    sessionService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()

                val currentState = sessionService.getStateByChatId(chatId)
                if (currentState.isAboutFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.MEETING_CREATE.command).build())
                    )
                    userService.update(userSession.draftAboutUser!!)
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                            keyboardRow(KeyboardButton.builder().text(CommandList.ACQUAINTANCE_INPUT_ABOUT_ME.command).build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваша информация о своей работе в анкете обновлена"
                b.execute(message2)
                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            else -> {
                b.silent().send("Неизвестное состояние. Сбрасываю диалог", chatId)
                sessionService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
        }

    }, Predicate { update ->
        setOf(
                CommandList.ACQUAINTANCE_INPUT_EMAIL.command, CommandList.ACQUAINTANCE_INPUT_PHONE.command, CommandList.ACQUAINTANCE_INPUT_NAME.command, CommandList.ACQUAINTANCE_INPUT_SURNAME.command, CommandList.ACQUAINTANCE_INPUT_ABOUT_ME.command, CommandList.ACQUAINTANCE_INPUT_ABOUT_WORK.command
        ).contains(update.message.text).not() &&
                sessionService.getStateByChatId(update.chatId()).let {
                    setOf(ChatState.INPUT_EMAIL, ChatState.INPUT_PHONE,
                            ChatState.INPUT_NAME, ChatState.INPUT_SURNAME,
                            ChatState.INPUT_ABOUT_JOB, ChatState.INPUT_ABOUT_ME).contains(it.currentChatState)
                }
    })
}
