package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.request.UserCreateRequest
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.service.UserService

@Component
class StartAbility : AbilityExtension {
    @Autowired
    lateinit var sessionService: SessionService

    @Autowired
    lateinit var userService: UserService

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
                    ctx.bot().execute(message)
                    val user = userService.getByTelegramUserId(ctx.user().id) ?: userService.create(UserCreateRequest(
                            ctx.user().id, ctx.user().userName
                    ))
                    val currentSession = sessionService.getStateByChatId(ctx.chatId())
                    sessionService.saveState(currentSession.copy(
                            userId = user.id,
                            telegramUserId = ctx.user().id,
                            telegramChatId = ctx.chatId(),
                            currentChatState = ChatState.NONE
                    ))
                }
                .build()
    }
}
