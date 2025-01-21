package gg.norisk.ffa.server.command

import net.silkmc.silk.commands.PermissionLevel
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.text.literalText

object KillCommand {
    fun init() {
        command("kill") {
            requiresPermissionLevel(PermissionLevel.NONE)
            runs {
                val player = this.source.playerOrThrow
                player.kill()
            }
        }

        command("me") {
            argument<String>("action") { action ->
                runs {
                    val player = this.source.playerOrThrow
                    this.source.playerOrThrow.sendMessage(literalText {
                        text(player.name)
                        text(": ")
                        text("Ich bin ein kompletter Versager")
                    })
                }
            }
        }
    }
}