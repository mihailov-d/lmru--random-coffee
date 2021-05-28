package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import ru.leroymerlin.random.coffee.core.util.chatId
import ru.leroymerlin.random.coffee.core.util.textEquals

@Component
class MeetingMatchAbility : AbilityExtension {
    fun publishMeetingReply(): Reply = Reply.of({ b, update ->
        b.silent().sendMd("Встреча опубликована", update.chatId())
        // TODO meeting publicate logic
    }, textEquals(CommandList.MATCH_MEETING_START.command))
}
