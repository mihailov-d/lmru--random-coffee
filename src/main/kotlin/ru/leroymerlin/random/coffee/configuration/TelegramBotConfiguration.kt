package ru.leroymerlin.random.coffee.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityUtils.getChatId
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.util.function.Predicate


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


class RandomCoffeeBot(private val botUsername: String, private val botToken: String) : AbilityBot(botToken, botUsername) {
    override fun getBotToken(): String {
        return botToken
    }

    override fun creatorId(): Long {
        return 727579598
    }

    override fun getBotUsername(): String {
        return botUsername
    }

    fun saysHelloWorldToFriend(): Ability {
        return Ability.builder()
                .name("sayhi")
                .info("Says hi")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .input(1)
                .action { ctx: MessageContext -> sender.execute(SendMessage.builder().chatId(ctx.chatId().toString()).text("Hi ${ctx.firstArg()}").build()) }
                .build()
    }

    fun startAbility(): Ability {
        return Ability.builder()
                .name("start")
                .info("Entry point")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext ->
                    val message = SendMessage()
                    // TODO if new user
                    val replyKeyboardMarkup = ReplyKeyboardMarkup()
                    val firstRow = KeyboardRow()
                    firstRow.add(KeyboardButton.builder().text("Заполнить карточку").build())
                    firstRow.add(KeyboardButton.builder().text("Хочу кофе").build())
                    replyKeyboardMarkup.keyboard = listOf(firstRow)
                    replyKeyboardMarkup.oneTimeKeyboard = true
                    message.replyMarkup = replyKeyboardMarkup
                    message.chatId = ctx.chatId().toString()
                    message.text = "Давай знакомиться"
                    sender.execute(message)
                }
                .build()
    }

    fun fillCardReply(): Reply {
        return Reply.of({ b, update ->
            val message = SendMessage()
            val removeKeyboard = ReplyKeyboardRemove()
            removeKeyboard.removeKeyboard = true
            message.replyMarkup = removeKeyboard
            message.chatId = getChatId(update).toString()
            message.text = "Got it"
            val e = execute(message)
        },
                Predicate { update -> update.hasMessage() && update.message.hasText() && update.message.text.contains("Заполнить карточку") })
    }

    fun createProfileAbility(): Ability {
        return Ability.builder()
                .name("createProfile")
                .info("create Profile")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun addPhoneAbility(): Ability {
        return Ability.builder()
                .name("addPhone")
                .info("add Phone")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun addEmailAbility(): Ability {
        return Ability.builder()
                .name("addEmail")
                .info("add Email")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun addTelegramAbility(): Ability {
        return Ability.builder()
                .name("addTelegram")
                .info("add Telegram")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun addHobbyInfoAbility(): Ability {
        return Ability.builder()
                .name("addHobbyInfo")
                .info("add Hobby Info")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun addJobInfoAbility(): Ability {
        return Ability.builder()
                .name("addJobInfo")
                .info("add Job Info")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }


    fun addMoreInfoAbility(): Ability {
        return Ability.builder()
                .name("addMoreInfo")
                .info("add More Info")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun startCoffeeReqAbility(): Ability {
        return Ability.builder()
                .name("startCoffeeReq")
                .info("start Coffee Req")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun coffeeForWorkAbility(): Ability {
        return Ability.builder()
                .name("coffeeForWork")
                .info("coffee For Work")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun coffeeForSoulAbility(): Ability {
        return Ability.builder()
                .name("coffeeForSoul")
                .info("coffee For Soul")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun addCoffeeDayAbility(): Ability {
        return Ability.builder()
                .name("addCoffeeDay")
                .info("add Coffee Day")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun findCoffeeFriendAbility(): Ability {
        return Ability.builder()
                .name("findCoffeeFriend")
                .info("find Coffee Friend")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun createCoffeeReqAbility(): Ability {
        return Ability.builder()
                .name("createCoffeeReq")
                .info("create Coffee Req")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun confirmMeetingAbility(): Ability {
        return Ability.builder()
                .name("confirmMeeting")
                .info("confirm Meeting")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun rejectMeetingAbility(): Ability {
        return Ability.builder()
                .name("rejectMeeting")
                .info("reject Meeting")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

    fun helpAbility(): Ability {
        return Ability.builder()
                .name("help")
                .info("help")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> silent.send("mock", ctx.chatId()) }
                .build()
    }

}
