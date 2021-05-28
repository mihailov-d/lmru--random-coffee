package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import ru.leroymerlin.random.coffee.core.util.stringChatId
import ru.leroymerlin.random.coffee.core.util.textEquals

@Component
class MeetingMatchAbility : AbilityExtension {
    fun publishMeetingReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Встреча опубликована"
        b.execute(message)
        // TODO meeting publicate logic
    }, textEquals(CommandList.MATCH_MEETING_START.command))
}
