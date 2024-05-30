package gg.norisk.ffa.client.selector.ui.components

import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen
import gg.norisk.ffa.network.selectorHeroPacket
import gg.norisk.heroes.common.hero.Hero
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.*
import io.wispforest.owo.ui.util.UISounds
import net.silkmc.silk.core.text.literal

class HeroListComponent(
    val heroes: List<Hero<*>>,
    val heroSelectorScreen: HeroSelectorScreen,
    horizontalSizing: Sizing = Sizing.content(),
    verticalSizing: Sizing = Sizing.content()
) :
    FlowLayout(
        horizontalSizing,
        verticalSizing,
        Algorithm.VERTICAL
    ) {
    init {
        gap(5)
        alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)

        val grid = Containers.grid(Sizing.content(), Sizing.content(), 1, heroes.size)
        for ((index, hero) in heroes.withIndex()) {
            grid.child(HeroHeadComponent(hero), 0, index)
        }
        child(grid)
        grid.surface(
            Surface.flat(
                java.awt.Color(java.awt.Color.WHITE.red, java.awt.Color.WHITE.blue, java.awt.Color.WHITE.green, 60).rgb
            )
        )
        grid.padding(Insets.of(5))

        val lockInButton = Components.button("LOCK IN".literal) {
            selectorHeroPacket.send(heroSelectorScreen.hero.internalKey)
        }
        lockInButton.horizontalSizing(Sizing.fixed(100))

        child(lockInButton)
    }

    inner class HeroHeadComponent(
        val hero: Hero<*>,
        horizontalSizing: Sizing = Sizing.content(),
        verticalSizing: Sizing = Sizing.content()
    ) : FlowLayout(
        horizontalSizing,
        verticalSizing,
        Algorithm.HORIZONTAL
    ) {
        init {
            val l = 8
            val m = 8
            val heroHead = Components.texture(hero.icon, 0, 0, 64, 64, 64, 64)
            //val heroHead2 = Components.texture(hero.skin, 40, l, 8, m, 64, 64)
            //OVERLAY
            heroHead.sizing(Sizing.fixed(32))
            child(heroHead)

            margins(Insets.of(2))
            padding(Insets.of(2))

            surface(Surface.outline(java.awt.Color.WHITE.darker().rgb))

            mouseDown().subscribe { _, _, _ ->
                UISounds.playButtonSound()
                heroSelectorScreen.hero = hero
                return@subscribe true
            }
            mouseEnter().subscribe {
                surface(Surface.outline(Color.WHITE.argb()))
            }
            mouseLeave().subscribe {
                surface(Surface.outline(java.awt.Color.WHITE.darker().rgb))
            }
        }
    }
}
