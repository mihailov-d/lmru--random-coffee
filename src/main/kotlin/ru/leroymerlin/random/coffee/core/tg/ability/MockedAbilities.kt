package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.leroymerlin.random.coffee.core.util.stringChatId
import java.util.function.Predicate

@Component
class MockedAbilities : AbilityExtension {

    fun createProfileAbility(): Ability {
        return Ability.builder()
                .name("createProfile")
                .info("create Profile")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun addPhoneAbility(): Ability {
        return Ability.builder()
                .name("addPhone")
                .info("add Phone")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun addEmailAbility(): Ability {
        return Ability.builder()
                .name("addEmail")
                .info("add Email")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun addTelegramAbility(): Ability {
        return Ability.builder()
                .name("addTelegram")
                .info("add Telegram")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun addHobbyInfoAbility(): Ability {
        return Ability.builder()
                .name("addHobbyInfo")
                .info("add Hobby Info")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun addJobInfoAbility(): Ability {
        return Ability.builder()
                .name("addJobInfo")
                .info("add Job Info")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }


    fun addMoreInfoAbility(): Ability {
        return Ability.builder()
                .name("addMoreInfo")
                .info("add More Info")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun startCoffeeReqAbility(): Ability {
        return Ability.builder()
                .name("startCoffeeReq")
                .info("start Coffee Req")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun coffeeForWorkAbility(): Ability {
        return Ability.builder()
                .name("coffeeForWork")
                .info("coffee For Work")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun coffeeForSoulAbility(): Ability {
        return Ability.builder()
                .name("coffeeForSoul")
                .info("coffee For Soul")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun addCoffeeDayAbility(): Ability {
        return Ability.builder()
                .name("addCoffeeDay")
                .info("add Coffee Day")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun findCoffeeFriendAbility(): Ability {
        // example with inline keyboard
        return Ability.builder()
                .name("test101")
                .info("find Coffee Friend")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext ->
                    val inlineKeyboardMarkup = InlineKeyboardMarkup()
                    inlineKeyboardMarkup.keyboard = listOf(
                            listOf(InlineKeyboardButton.builder().callbackData("data").text("Online101").build()),
                            listOf(InlineKeyboardButton.builder().callbackData("data101").text("Offline").build())
                    )

                    val message = SendMessage()
                    message.chatId = ctx.update().stringChatId()
                    message.text = "Try with ability"
                    message.replyMarkup = inlineKeyboardMarkup
                    ctx.bot().execute(message)
                }
                .post { ctx: MessageContext ->
                    println("post")
                }
                .build()
    }

    fun online101(): Reply = Reply.of({ b, update ->
        // example edit message
        val editMessageText = EditMessageText()
        editMessageText.chatId = update.stringChatId()
        editMessageText.messageId = update.callbackQuery.message.messageId
        editMessageText.text = "Ok, you push ${update.callbackQuery.data} and text `${update.callbackQuery.message.text}`"
        b.execute(editMessageText)
    }, Predicate { update -> update.hasCallbackQuery() && false })

    fun confirmMeetingAbility(): Ability {
        return Ability.builder()
                .name("confirmMeeting")
                .info("confirm Meeting")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun rejectMeetingAbility(): Ability {
        return Ability.builder()
                .name("rejectMeeting")
                .info("reject Meeting")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

}
