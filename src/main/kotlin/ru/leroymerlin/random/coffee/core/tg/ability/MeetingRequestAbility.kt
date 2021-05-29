package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.leroymerlin.random.coffee.core.dto.MeetingStatusEnum.RANDOM
import ru.leroymerlin.random.coffee.core.service.MeetingService
import ru.leroymerlin.random.coffee.core.service.RandomService
import ru.leroymerlin.random.coffee.core.service.UserService
import ru.leroymerlin.random.coffee.core.tg.sender.MeetingRequestSender
import ru.leroymerlin.random.coffee.core.util.message.MessageUtil
import ru.leroymerlin.random.coffee.core.util.message.MessageUtil.communicationChannelString
import ru.leroymerlin.random.coffee.core.util.stringChatId
import java.util.UUID
import java.util.function.Predicate

@Component
class MeetingRequestAbility : AbilityExtension {

    @Autowired
    lateinit var meetingRequestSender: MeetingRequestSender

    @Autowired
    lateinit var meetingService: MeetingService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var randomService: RandomService

    fun meetingRequestReply(): Reply = Reply.of({ b, update ->
        // meetingId and "approve" or "nope"
        val splitData = update.callbackQuery.data.split("__").let {
            Pair(UUID.fromString(it[1]), it[2])
        }
        val meetingId = splitData.first

        val meeting = meetingService.get(meetingId)

        val userCreatedMeeting = userService.get(meeting.userId)
        var otherUser = userService.get(if (meeting.status == RANDOM) meeting.requestFromMeetingId!! else meeting.requestToMeetingId!!)

        val editMessageText = EditMessageText()
        editMessageText.chatId = update.stringChatId()
        editMessageText.messageId = update.callbackQuery.message.messageId
        // there is a problem with edit message with markdown, it doesn't work,
        // you should manualy define text again with markdown
        if (splitData.second == "approve") {
            editMessageText.text = """
            Предложение о встрече c "${userCreatedMeeting.name} ${userCreatedMeeting.surname}", ${meeting.preferDate.toLocalDate()}
            Анкета 📑
            Обо мне:
            ${userCreatedMeeting.aboutMe}
            О работе:
            ${userCreatedMeeting.aboutJob}

            Тема для встречи: '${MessageUtil.meetingTopicMessageString(meeting.topicTypeEnum)}'
            
            Вы подтвердили встречу ✅
            
            Свяжитесь друг с другом по указанным контактным данным: ${MessageUtil.getContact(userCreatedMeeting)}
        """.trimIndent()
            // TODO in background
            // TODO link and change meeting status
            randomService.approve(meetingId)
            meetingRequestSender.sendSuccess(splitData.first)
        } else if (splitData.second == "nope") {
            editMessageText.text = """
            Предложение о встрече c "${userCreatedMeeting.name} ${userCreatedMeeting.surname}", ${meeting.preferDate.toLocalDate()}
            Анкета 📑
            Обо мне:
            ${userCreatedMeeting.aboutMe}
            О работе:
            ${userCreatedMeeting.aboutJob}

            Тема для встречи: '${MessageUtil.meetingTopicMessageString(meeting.topicTypeEnum)}'
            
            Вы отклонили встречу ❌
        """.trimIndent()
            // TODO change meeting status
            randomService.nope(meetingId)
        }
//        editMessageText.enableMarkdown(true)
        b.execute(editMessageText)
    }, Predicate { update -> update.hasCallbackQuery() && update.callbackQuery.data.startsWith("mr__") })

}
