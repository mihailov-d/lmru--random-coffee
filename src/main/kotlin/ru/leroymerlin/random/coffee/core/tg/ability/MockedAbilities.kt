package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.util.AbilityExtension

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
        return Ability.builder()
                .name("findCoffeeFriend")
                .info("find Coffee Friend")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

    fun createCoffeeReqAbility(): Ability {
        return Ability.builder()
                .name("createCoffeeReq")
                .info("create Coffee Req")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

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

    fun helpAbility(): Ability {
        return Ability.builder()
                .name("help")
                .info("help")
                .privacy(Privacy.PUBLIC)
                .locality(Locality.USER)
                .action { ctx: MessageContext -> ctx.bot().silent().send("mock", ctx.chatId()) }
                .build()
    }

}
