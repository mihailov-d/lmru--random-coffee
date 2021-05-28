package ru.leroymerlin.random.coffee.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

import org.telegram.telegrambots.meta.api.methods.send.SendMessage




@Configuration
class TelegramBotConfiguration {
    @Bean
    fun telegramBot(@Value("\${telegram-bot.username}") botUsername: String, @Value("\${telegram-bot.token}") botToken: String): TelegramBotsApi {
        val tg = TelegramBotsApi(DefaultBotSession::class.java)
        val telegramLongPollingBot = RandomCoffeeBot(botUsername, botToken)
        tg.registerBot(telegramLongPollingBot)
        return tg
    }
}


class RandomCoffeeBot(private val botUsername: String, private val botToken: String): TelegramLongPollingBot() {
    override fun getBotToken(): String {
        return botToken
    }

    override fun getBotUsername(): String {
        return botUsername
    }

    override fun onUpdateReceived(update: Update) {
        val message = SendMessage() // Create a SendMessage object with mandatory fields

        message.chatId = update.message.chatId.toString()
        message.text = "lol kek cheburek"

        try {
            execute(message) // Call method to send the message
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

}
