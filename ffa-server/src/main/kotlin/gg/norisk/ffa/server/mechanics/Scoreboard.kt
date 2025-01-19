package gg.norisk.ffa.server.mechanics

import gg.norisk.heroes.common.db.DatabaseManager.dbPlayer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Colors
import net.silkmc.silk.core.text.literalText
import net.silkmc.silk.game.sideboard.Sideboard
import net.silkmc.silk.game.sideboard.sideboard
import java.awt.Color
import kotlin.time.Duration.Companion.seconds

object Scoreboard {
    fun getScoreboardForPlayer(player: ServerPlayerEntity): Sideboard {
        val mainColor = Color(36, 173, 227).rgb
        val secondaryColor = Color(150, 198, 207).rgb
        return sideboard(literalText {
            text("HG") {
                color = mainColor
                bold = true
            }
            text("Labor.de") {
                bold = true
            }
            text(" | ") {
                color = secondaryColor
            }
            text("FFA")
        }) {
            line(literalText("                           ") {
                strikethrough = true
                color = Color.LIGHT_GRAY.rgb
            })
            emptyLine()
            updatingLine(1.seconds) {
                literalText {
                    text("Kills") {
                        color = mainColor
                    }
                    text(": ") {
                        color = secondaryColor
                    }
                    text(player.dbPlayer.kills.toString())
                }
            }
            updatingLine(1.seconds) {
                literalText {
                    text("Deaths") {
                        color = mainColor
                    }
                    text(": ") {
                        color = secondaryColor
                    }
                    text(player.dbPlayer.deaths.toString())
                }
            }
            updatingLine(1.seconds) {
                literalText {
                    text("Streak") {
                        color = mainColor
                    }
                    text(": ") {
                        color = secondaryColor
                    }
                    text(player.dbPlayer.currentKillStreak.toString())
                }
            }
            updatingLine(1.seconds) {
                literalText {
                    text("Xp") {
                        color = mainColor
                    }
                    text(": ") {
                        color = secondaryColor
                    }
                    text(player.dbPlayer.xp.toString())
                }
            }
            emptyLine()
            line(literalText("                           ") {
                strikethrough = true
                color = Color.LIGHT_GRAY.rgb
            })
        }
    }
}