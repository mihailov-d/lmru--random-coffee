package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Reply
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.leroymerlin.random.coffee.core.util.stringChatId
import java.util.UUID
import java.util.function.Predicate

@Component
class MeetingRequestAbility : AbilityExtension {

    fun meetingRequestReply(): Reply = Reply.of({ b, update ->
        // meetingId and "approve" or "nope"
        val splitData = update.callbackQuery.data.split("__").let {
            Pair(UUID.fromString(it[1]), it[2])
        }

        val editMessageText = EditMessageText()
        editMessageText.chatId = update.stringChatId()
        editMessageText.messageId = update.callbackQuery.message.messageId
        // there is a problem with edit message with markdown, it doesn't work,
        // you should manualy define text again with markdown
        if (splitData.second == "approve") {
            editMessageText.text = """
                ${update.callbackQuery.message.text}
                
                ✅ 
            """.trimIndent()
        } else if (splitData.second == "nope") {
            editMessageText.text = """
                ${update.callbackQuery.message.text}
                
                ❌ 
            """.trimIndent()
        }
        b.execute(editMessageText)
        editMessageText.enableMarkdown(true)
    }, Predicate { update -> update.hasCallbackQuery() && update.callbackQuery.data.startsWith("mr__") })

}
