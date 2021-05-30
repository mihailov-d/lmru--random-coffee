package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import ru.leroymerlin.random.coffee.core.dto.ChatState
import ru.leroymerlin.random.coffee.core.dto.request.MeetingCreateRequest
import ru.leroymerlin.random.coffee.core.service.MeetingService
import ru.leroymerlin.random.coffee.core.service.RandomService
import ru.leroymerlin.random.coffee.core.service.SessionService
import ru.leroymerlin.random.coffee.core.util.chatId
import ru.leroymerlin.random.coffee.core.util.stringChatId
import ru.leroymerlin.random.coffee.core.util.textEquals

@Component
class MeetingMatchAbility : AbilityExtension {
    @Autowired
    lateinit var meetingService: MeetingService

    @Autowired
    lateinit var sessionService: SessionService

    @Autowired
    lateinit var randomService: RandomService

    fun publishMeetingReply(): Reply = Reply.of({ b, update ->
        val message = SendMessage()
        message.replyMarkup = ReplyKeyboardRemove(true)
        message.chatId = update.stringChatId()
        message.text = "Встреча опубликована"
        val sessionDto = sessionService.getStateByChatId(update.chatId())
        // TODO check draft not null!!
        val draftMeeting = sessionDto.draftMeeting
        val meeting = meetingService.create(MeetingCreateRequest(sessionDto.userId, draftMeeting!!.topicType!!, draftMeeting.preferDate!!))
        randomService.random(meeting)
        sessionService.saveState(sessionDto.copy(draftMeeting = null, currentChatState = ChatState.NONE))
        b.execute(message)
        // TODO meeting publicate logic
    }, textEquals(CommandList.MATCH_MEETING_START.command))
}
