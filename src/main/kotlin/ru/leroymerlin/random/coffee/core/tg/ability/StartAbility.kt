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
import ru.leroymerlin.random.coffee.core.repository.MeetingRepository
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.service.UserService
import ru.leroymerlin.random.coffee.core.tg.sender.MeetingRequestSender

@Component
class StartAbility : AbilityExtension {
    @Autowired
    lateinit var sessionService: SessionService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var meetingRequestSender: MeetingRequestSender

    @Autowired
    lateinit var meetingRepository: MeetingRepository

    fun startAbility(): Ability {
        return Ability.builder()
                .name("start")
                .info("Entry point")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext ->
                    val tgChatId = ctx.chatId()

                    val user = userService.getByTelegramUserId(ctx.user().id) ?: userService.create(UserCreateRequest(
                            ctx.user().id, ctx.user().userName
                    ))
                    val currentSession = sessionService.getStateByChatId(tgChatId)
                    sessionService.saveState(currentSession.copy(
                            userId = user.id,
                            telegramUserId = ctx.user().id,
                            telegramChatId = tgChatId,
                            currentChatState = ChatState.NONE
                    ))
                    val profileIsFill = currentSession.isAboutFill() && currentSession.isCommunicationFill() && currentSession.isNameAndSurnameFill()

                    val message = SendMessage()
                    val replyKeyboardMarkup = ReplyKeyboardMarkup()
                    val firstRow = KeyboardRow()
                    firstRow.add(KeyboardButton.builder().text(if (profileIsFill) CommandList.ACQUAINTANCE_FILL_CARD_NEW_USER.command else CommandList.ACQUAINTANCE_FILL_CARD.command).build())
                    if (profileIsFill) {
                        firstRow.add(KeyboardButton.builder().text(CommandList.MEETING_CREATE_FROM_START.command).build())
                    }
                    replyKeyboardMarkup.keyboard = listOf(firstRow)
                    replyKeyboardMarkup.oneTimeKeyboard = true
                    message.replyMarkup = replyKeyboardMarkup
                    message.chatId = tgChatId.toString()
                    message.text = """
                        Привет! 👋
                        
                        Я бот LM Random Coffee, моя миссия – помогать коллегам найти интересных собеседников за чашечкой кофе!
                    """.trimIndent()
                    ctx.bot().execute(message)
                }
                .build()
    }

    fun startTestAbility(): Ability {
        return Ability.builder()
                .name("start_test")
                .info("start test")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext ->
                    val tgChatId = ctx.chatId()
                    val meeting = meetingRepository.findAll().random()

                    meetingRequestSender.sendPropose(tgChatId, meeting.id)
                }
                .build()
    }
}
