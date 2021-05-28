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
import ru.leroymerlin.random.coffee.core.service.UserSessionStateService
import ru.leroymerlin.random.coffee.core.util.chatId
import ru.leroymerlin.random.coffee.core.util.keyboardRow
import ru.leroymerlin.random.coffee.core.util.stringChatId
import ru.leroymerlin.random.coffee.core.util.textEquals
import java.util.function.Predicate

@Component
class AcquaintanceAbility : AbilityExtension {

    @Autowired
    lateinit var userSessionStateService: UserSessionStateService

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

    fun typeTelegramReply(): Reply = Reply.of({ b, update ->
        userSessionStateService.updateChatStateByChatId(update.chatId(), ChatState.NONE)
        // TODO save telegram id as contact type
        val userName = update.message.chat.userName

        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.enableMarkdownV2(true)
        message.text = "Спасибо, мы сохранили ваш telegramName как контактный: @$userName"
        b.execute(message)
    }, textEquals("Телеграм"))

    fun commonTextAbility(): Reply = Reply.of({ b, update ->
        val chatId = update.chatId()

        when (userSessionStateService.getChatStateByChatId(chatId)) {
            ChatState.INPUT_EMAIL -> {
                val userEmail = update.message.text.trim()
                // TODO email validation
                b.silent().sendMd("Спасибо, мы сохранили твой email: $userEmail", chatId)
                // TODO save user email
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            ChatState.INPUT_PHONE -> {
                val userPhone = update.message.text.trim()
                // TODO phone validation
                b.silent().sendMd("Спасибо, мы сохранили твой телефон: $userPhone", chatId)
                // TODO save user phone
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
            else -> {
                b.silent().send("Неизвестное состояние. Сбрасываю диалог", chatId)
                userSessionStateService.updateChatStateByChatId(chatId, ChatState.NONE)
            }
        }

    }, Predicate { update ->
        setOf("Почта", "Телефон").contains(update.message.text).not() &&
                userSessionStateService.getStateByChatId(update.chatId())?.let { setOf(ChatState.INPUT_EMAIL, ChatState.INPUT_PHONE).contains(it.currentChatState) }
                ?: false
    })
}
