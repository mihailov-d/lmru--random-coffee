package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.util.AbilityExtension
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.leroymerlin.random.coffee.core.util.stringChatId

@Component
class HelpAbility : AbilityExtension {
    //Команды которые доступны
    val commandWithDescriptionMap = mapOf(
        "/help" to "Описание команд \n",
        "/meeting_list" to "Посмотреть перечень всех встреч, созданных тобой и принятых коллегами\n",
        "/create_coffee_request" to "Создать запрос на встречу\n",
        "/start" to "Начать работу с ботом или начать с начала с любого места\n")
    fun helpAbility(): Ability = Ability.builder()
        .name("help")
        .info("Help")
        .privacy(Privacy.PUBLIC)
        .locality(Locality.USER)
        .input(0)
        .action { ctx: MessageContext ->
            val message = SendMessage()
            message.chatId = ctx.update().stringChatId()
            message.text = commandWithDescriptionMap.toString()
                .replace(",","")
                .replace("{", "")
                .replace("}", "")
                .replace("=","\t->\t")
            ctx.bot().execute(message)
        }
        .build()

}
