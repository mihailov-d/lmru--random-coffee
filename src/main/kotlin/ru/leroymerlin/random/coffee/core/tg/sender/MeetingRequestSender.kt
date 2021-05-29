package ru.leroymerlin.random.coffee.core.tg.sender

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.leroymerlin.random.coffee.configuration.RandomCoffeeBot
import ru.leroymerlin.random.coffee.core.util.TgChatId
import java.util.UUID

@Component
class MeetingRequestSender {

    @Autowired
    lateinit var randomCoffeeBot: RandomCoffeeBot

    fun sendPropose(chatId: TgChatId) {
        val name = "Sej"
        val meetingId = UUID.randomUUID()
        val aboutMe = """Супер паренек, возбуждаюсь от велосипедов, сквончу на походы и мечтаю о полете на Плутон"""

        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = """
            Предложение о встрече c `$name`
            
            ```
            $aboutMe
            ```
        """.trimIndent()
        message.enableMarkdown(true)


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
}
