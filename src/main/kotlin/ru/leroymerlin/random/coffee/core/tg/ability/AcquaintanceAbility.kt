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
import ru.leroymerlin.random.coffee.core.util.chatId
import ru.leroymerlin.random.coffee.core.util.keyboardRow
import ru.leroymerlin.random.coffee.core.util.stringChatId
import ru.leroymerlin.random.coffee.core.util.textEquals
import java.util.UUID
import java.util.function.Predicate

@Component
class AcquaintanceAbility : AbilityExtension {

    @Autowired
    lateinit var userSessionStateService: SessionService

    fun fillCardReply(): Reply {
        return Reply.of({ b, update ->
            val message = SendMessage()
            val replyKeyboardMarkup = ReplyKeyboardMarkup()
            replyKeyboardMarkup.keyboard = listOf(
                keyboardRow(KeyboardButton.builder().text("Почта").build()),
                keyboardRow(KeyboardButton.builder().text("Телефон").build()),
                keyboardRow(KeyboardButton.builder().text("Телеграм").build())
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

        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_EMAIL)
    }, textEquals("Почта"))

    fun typePhoneReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите ваш телефон, по которому с вами можно связаться для встречи"
        b.execute(message)

        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_PHONE)
    }, textEquals("Телефон"))

    fun typeInputNameReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите ваше имя для анкеты"
        b.execute(message)

        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_NAME)
    }, textEquals("Ввести имя"))

    fun typeInputSurnameReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите вашу фамилию для анкеты"
        b.execute(message)

        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_SURNAME)
    }, textEquals("Ввести фамилию"))

    fun typeAboutMeReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите информацию о себе для анкеты"
        b.execute(message)

        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_ABOUT_ME)
    }, textEquals("Ввести информацию о себе"))

    fun typeAboutJobReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Введите информацию о работе для анкеты"
        b.execute(message)

        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.INPUT_ABOUT_JOB)
    }, textEquals("Ввести информацию о работе"))

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

        userSessionStateService.getStateByChatId(update.chatId())?.apply {
            val updatedSession = this.copy(
                draftCommunicationUser = this.draftCommunicationUser?.copy(
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
            userSessionStateService.saveState(updatedSession)
        }
        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.NONE)

        val message2 = SendMessage()
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.keyboard = listOf(
            keyboardRow(KeyboardButton.builder().text("Ввести имя").build()),
            keyboardRow(KeyboardButton.builder().text("Ввести фамилию").build())
        )
        message2.replyMarkup = replyKeyboardMarkup
        message2.chatId = update.stringChatId()
        message2.text = "Введите информацию о себе"
        b.execute(message2)
    }, textEquals("Телеграм"))

    fun commonTextAbility(): Reply = Reply.of({ b, update ->
        val chatId = update.chatId()

        when (userSessionStateService.getChatStateByChatId(chatId)) {
            ChatState.INPUT_EMAIL -> {
                val userEmail = update.message.text.trim()
                // TODO email validation
                b.silent().sendMd("Спасибо, мы сохранили твой email: `$userEmail`", chatId)
                userSessionStateService.getStateByChatId(update.chatId())?.apply {
                    val updatedSession = this.copy(
                        draftCommunicationUser = this.draftCommunicationUser?.copy(
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
                    userSessionStateService.saveState(updatedSession)
                }
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_PHONE -> {
                val userPhone = update.message.text.trim()
                // TODO phone validation
                b.silent().sendMd("Спасибо, мы сохранили твой телефон: `$userPhone`", chatId)
                userSessionStateService.getStateByChatId(update.chatId())?.apply {
                    val updatedSession = this.copy(
                        draftCommunicationUser = this.draftCommunicationUser?.copy(
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
                    userSessionStateService.saveState(updatedSession)
                }

                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_NAME -> {
                val name = update.message.text.trim()
                // TODO name validation

                userSessionStateService.getStateByChatId(update.chatId())?.apply {
                    val updatedSession = this.copy(
                        draftBasicUser = this.draftBasicUser?.copy(name = name)
                            ?: UserBasicUpdateRequest(UUID.randomUUID(), name, null)
                    )
                    userSessionStateService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()
                val currentState = userSessionStateService.getStateByChatId(update.chatId())!!
                if (currentState.isNameAndSurnameFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Ввести информацию о себе").build()),
                        keyboardRow(KeyboardButton.builder().text("Ввести информацию о работе").build())
                    )
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Ввести фамилию").build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваше имя для анкеты: $name"
                b.execute(message2)
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_SURNAME -> {
                val surname = update.message.text.trim()
                // TODO surname validation

                userSessionStateService.getStateByChatId(update.chatId())?.apply {
                    val updatedSession = this.copy(
                        draftBasicUser = this.draftBasicUser?.copy(surname = surname)
                            ?: UserBasicUpdateRequest(UUID.randomUUID(), null, surname)
                    )
                    userSessionStateService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()

                val currentState = userSessionStateService.getStateByChatId(update.chatId())!!
                if (currentState.isNameAndSurnameFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Ввести информацию о себе").build()),
                        keyboardRow(KeyboardButton.builder().text("Ввести информацию о работе").build())
                    )
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Ввести имя").build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваша фамилия для анкеты: $surname"
                b.execute(message2)
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_ABOUT_ME -> {
                val aboutMe = update.message.text.trim()

                userSessionStateService.getStateByChatId(update.chatId())?.apply {
                    val updatedSession = this.copy(
                        draftAboutUser = this.draftAboutUser?.copy(aboutMe = aboutMe)
                            ?: UserAboutUpdateRequest(UUID.randomUUID(), aboutMe, null)
                    )
                    userSessionStateService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()

                val currentState = userSessionStateService.getStateByChatId(update.chatId())!!
                if (currentState.isAboutFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Создать встречу").build())
                    )
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Ввести информацию о работе").build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваша информация о себе в анкете обновлена"
                b.execute(message2)
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_ABOUT_JOB -> {
                val aboutJob = update.message.text.trim()

                userSessionStateService.getStateByChatId(update.chatId())?.apply {
                    val updatedSession = this.copy(
                        draftAboutUser = this.draftAboutUser?.copy(aboutJob = aboutJob)
                            ?: UserAboutUpdateRequest(UUID.randomUUID(), null, aboutJob)
                    )
                    userSessionStateService.saveState(updatedSession)
                }

                val message2 = SendMessage()
                val replyKeyboardMarkup = ReplyKeyboardMarkup()

                val currentState = userSessionStateService.getStateByChatId(update.chatId())!!
                if (currentState.isAboutFill()) {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Создать встречу").build())
                    )
                } else {
                    replyKeyboardMarkup.keyboard = listOf(
                        keyboardRow(KeyboardButton.builder().text("Ввести информацию о себе").build())
                    )
                }
                message2.replyMarkup = replyKeyboardMarkup
                message2.chatId = update.stringChatId()
                message2.text = "Ваша информация о своей работе в анкете обновлена"
                b.execute(message2)
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            else -> {
                b.silent().send("Неизвестное состояние. Сбрасываю диалог", chatId)
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
        }

    }, Predicate { update ->
        setOf(
            "Почта",
            "Телефон",
            "Ввести имя",
            "Ввести фамилию",
            "Ввести информацию о себе",
            "Ввести информацию о работе"
        ).contains(update.message.text).not() &&
                userSessionStateService.getStateByChatId(update.chatId())?.let {
                    setOf(
                        ChatState.INPUT_EMAIL, ChatState.INPUT_PHONE,
                        ChatState.INPUT_NAME, ChatState.INPUT_SURNAME,
                        ChatState.INPUT_ABOUT_JOB, ChatState.INPUT_ABOUT_ME
                    ).contains(it.currentChatState)
                } ?: false
    })
}
