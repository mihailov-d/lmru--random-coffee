package ru.leroymerlin.random.coffee.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.toggle.AbilityToggle
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession


@Configuration
class TelegramBotConfiguration {

    @Bean
    fun telegramBot(@Value("\${telegram-bot.username}") botUsername: String,
                    @Value("\${telegram-bot.token}") botToken: String,
                    abilities: List<AbilityExtension>): TelegramBotsApi {
        val tg = TelegramBotsApi(DefaultBotSession::class.java)
        val telegramLongPollingBot = RandomCoffeeBot(botUsername, botToken, abilities)
        tg.registerBot(telegramLongPollingBot)
        return tg
    }
}

class RandomCoffeeAbilityToggle : AbilityToggle {
    override fun isOff(ab: Ability?): Boolean {
        return true
    }

    override fun processAbility(ab: Ability?): Ability {
        return ab!!
    }
}

class RandomCoffeeBot(private val botUsername: String, private val botToken: String, private val abilities: List<AbilityExtension>) :
        AbilityBot(botToken, botUsername, RandomCoffeeAbilityToggle()) {
    init {
        addExtensions(abilities)
    }

    override fun getBotToken(): String {
        return botToken
    }

    override fun creatorId(): Long {
        return 727579598
    }

    override fun getBotUsername(): String {
        return botUsername
    }
}
