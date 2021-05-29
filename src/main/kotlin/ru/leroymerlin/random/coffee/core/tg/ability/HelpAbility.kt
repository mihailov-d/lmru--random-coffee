package ru.leroymerlin.random.coffee.core.tg.ability

import org.springframework.stereotype.Component
import org.telegram.abilitybots.api.objects.Ability
import org.telegram.abilitybots.api.objects.Locality
import org.telegram.abilitybots.api.objects.MessageContext
import org.telegram.abilitybots.api.objects.Privacy
import org.telegram.abilitybots.api.util.AbilityExtension

@Component
class HelpAbility : AbilityExtension {
    fun helpAbility(): Ability = Ability.builder()
            .name("help")
            .info("Help")
            .privacy(Privacy.PUBLIC)
            .locality(Locality.USER)
            .input(0)
            .action { ctx: MessageContext ->
                val ab = ctx.bot().abilities()
            }
            .build()
}
