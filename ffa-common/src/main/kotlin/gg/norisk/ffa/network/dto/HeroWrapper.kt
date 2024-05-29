package gg.norisk.ffa.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class HeroWrapper(
    val name: String, val icon: String
)
