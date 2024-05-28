package gg.norisk.ffa.client.selector.ui

import gg.norisk.ffa.Heroes
import gg.norisk.ffa.client.selector.ui.components.HeroInfoComponent
import gg.norisk.ffa.client.selector.ui.components.HeroListComponent
import io.wispforest.owo.ui.base.BaseOwoScreen
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.OwoUIAdapter
import io.wispforest.owo.ui.core.Positioning

class HeroSelectorScreen : BaseOwoScreen<FlowLayout>() {
    var hero = Heroes.entries.first()
        set(value) {
            field = value
            heroInfoComponent.remove()
            heroInfoComponent = HeroInfoComponent(hero).apply { positioning(Positioning.relative(100, 10)) }
            this.uiAdapter.rootComponent.child(heroInfoComponent)
        }
    var heroInfoComponent = HeroInfoComponent(hero).apply { positioning(Positioning.relative(100, 10)) }

    override fun createAdapter(): OwoUIAdapter<FlowLayout> {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    override fun build(root: FlowLayout) {
        val heroList = HeroListComponent(Heroes.entries, this)
        heroList.positioning(Positioning.relative(50, 90))

        root.child(heroList)
        root.child(heroInfoComponent)
    }

    override fun shouldPause(): Boolean {
        return false
    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }
}
