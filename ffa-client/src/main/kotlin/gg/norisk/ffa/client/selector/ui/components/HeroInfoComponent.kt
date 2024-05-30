package gg.norisk.ffa.client.selector.ui.components

import com.thedeanda.lorem.LoremIpsum
import gg.norisk.ffa.utils.ChatUtils
import gg.norisk.heroes.common.hero.Hero
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.HorizontalAlignment
import io.wispforest.owo.ui.core.Sizing
import io.wispforest.owo.ui.core.Surface
import io.wispforest.owo.ui.core.VerticalAlignment
import io.wispforest.owo.ui.util.UISounds
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.silkmc.silk.core.text.literal
import net.silkmc.silk.core.text.literalText
import java.awt.Color
import kotlin.random.Random

class HeroInfoComponent(
    val hero: Hero<*>,
    horizontalSizing: Sizing = Sizing.content(),
    verticalSizing: Sizing = Sizing.content()
) : FlowLayout(
    horizontalSizing,
    verticalSizing,
    Algorithm.VERTICAL
) {
    private var heroAbilityDescriptionComponent = HeroAbilityDescriptionComponent(hero)
    private val heroAbilities = buildList {
        val keyBindings = listOf(
            MinecraftClient.getInstance().options.chatKey,
            MinecraftClient.getInstance().options.forwardKey,
            MinecraftClient.getInstance().options.togglePerspectiveKey,
            MinecraftClient.getInstance().options.backKey,
            MinecraftClient.getInstance().options.dropKey,
            MinecraftClient.getInstance().options.playerListKey,
        )
        repeat(5) {
            add(
                HeroAbilityComponent(
                    keyBindings.random(),
                    Registries.ITEM.get(Random.nextInt(Registries.ITEM.size())).defaultStack
                )
            )
        }
    }
    private var currentAbility: HeroAbilityComponent = heroAbilities.first()
        set(value) {
            field = value
            heroAbilityDescriptionComponent.remove()
            heroAbilityDescriptionComponent = HeroAbilityDescriptionComponent(hero)
            child(heroAbilityDescriptionComponent)
        }

    init {
        val name = Components.label(literalText(hero.name) {
            bold = true
            color = Color.YELLOW.brighter().rgb
        })
        name.shadow(true)

        /*val nameWrapper = Containers.renderEffect(name)
        nameWrapper.effect(RenderEffectWrapper.RenderEffect.transform {
            val scale = 1f
            it.scale(scale, scale, scale)
            it.translate(
                -(nameWrapper.x() - (nameWrapper.x() / scale)),
                -(nameWrapper.y() - (nameWrapper.y() / scale)), 0f
            )
        })*/

        child(name)

        val horizontalAbilityWrapper = Containers.horizontalFlow(Sizing.content(), Sizing.content())
        horizontalAbilityWrapper.gap(2)
        for (heroAbility in heroAbilities) {
            horizontalAbilityWrapper.child(heroAbility)
        }


        child(horizontalAbilityWrapper)

        /*val descriptionWrapper = Containers.renderEffect(HeroAbilityDescriptionComponent(hero))
        descriptionWrapper.effect(RenderEffectWrapper.RenderEffect.transform {
            val scale = 0.6f
            it.scale(scale, scale, scale)
            it.translate(
                -(descriptionWrapper.x() - (descriptionWrapper.x() / scale)),
                -(descriptionWrapper.y() - (descriptionWrapper.y() / scale)), 0f
            )
        })*/


        child(heroAbilityDescriptionComponent)
    }

    private class HeroAbilityDescriptionComponent(
        val hero: Hero<*>,
        horizontalSizing: Sizing = Sizing.content(),
        verticalSizing: Sizing = Sizing.content()
    ) : FlowLayout(
        horizontalSizing,
        verticalSizing,
        Algorithm.VERTICAL
    ) {
        init {
            val generalDescription = Components.label(literalText(LoremIpsum.getInstance().getWords(5, 20)) {
                color = Color.YELLOW.brighter().rgb
            })
            generalDescription.shadow(true)
            generalDescription.maxWidth(150)
            child(generalDescription)

            child(HeroAbilitySkillTreeComponent())
        }
    }

    private class HeroAbilitySkillTreeComponent(
        horizontalSizing: Sizing = Sizing.content(),
        verticalSizing: Sizing = Sizing.content()
    ) : FlowLayout(
        horizontalSizing,
        verticalSizing,
        Algorithm.HORIZONTAL
    ) {
        init {
            alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            val decrease = Components.button("-".literal) {}
            val increase = Components.button("+".literal) {}
            val progressBar = Components.label(literalText {
                text(ChatUtils.getProgressBar(20, 100, 20, "|".single()))
            })
            child(decrease)
            child(progressBar)
            child(increase)
        }
    }

    inner class HeroAbilityComponent(
        val keyBinding: KeyBinding,
        val itemStack: ItemStack,
        horizontalSizing: Sizing = Sizing.content(),
        verticalSizing: Sizing = Sizing.content()
    ) : FlowLayout(
        horizontalSizing,
        verticalSizing,
        Algorithm.VERTICAL
    ) {
        init {
            alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)

            val size = 32

            val keyBinding = Components.label(keyBinding.boundKeyLocalizedText)
            keyBinding.shadow(true)
            val keyBindingWrapper = Containers.horizontalFlow(Sizing.fixed(size), Sizing.content())
            keyBindingWrapper.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            keyBindingWrapper.child(keyBinding)
            keyBindingWrapper.surface(Surface.flat(Color(Color.WHITE.red, Color.WHITE.blue, Color.WHITE.green, 60).rgb))


            val itemStack = Components.item(itemStack)
            val itemWrapper = Containers.horizontalFlow(Sizing.fixed(size), Sizing.fixed(size))
            itemWrapper.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            itemWrapper.child(itemStack)
            itemWrapper.surface(Surface.flat(Color(Color.WHITE.red, Color.WHITE.blue, Color.WHITE.green, 60).rgb))

            child(keyBindingWrapper)
            child(itemWrapper)

            mouseDown().subscribe { _, _, _ ->
                UISounds.playButtonSound()
                currentAbility = this
                return@subscribe true
            }

            mouseEnter().subscribe {
                keyBindingWrapper.surface(
                    Surface.flat(
                        Color(
                            Color.WHITE.red,
                            Color.WHITE.blue,
                            Color.WHITE.green,
                            180
                        ).rgb
                    )
                )
                itemWrapper.surface(Surface.flat(Color(Color.WHITE.red, Color.WHITE.blue, Color.WHITE.green, 110).rgb))
            }
            mouseLeave().subscribe {
                keyBindingWrapper.surface(
                    Surface.flat(
                        Color(
                            Color.WHITE.red,
                            Color.WHITE.blue,
                            Color.WHITE.green,
                            60
                        ).rgb
                    )
                )
                itemWrapper.surface(Surface.flat(Color(Color.WHITE.red, Color.WHITE.blue, Color.WHITE.green, 60).rgb))
            }
        }
    }
}
