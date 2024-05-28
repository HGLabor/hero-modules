package gg.norisk.ffa

import gg.norisk.ffa.FFACommon.toId

enum class Heroes {
    IRONMAN, SPIDERMAN, HULK, SUPERMAN, DARTH_VADER;

    val skin get() = "textures/heroes/${name.lowercase()}.png".toId()
}
