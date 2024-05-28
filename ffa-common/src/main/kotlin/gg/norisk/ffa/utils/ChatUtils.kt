package gg.norisk.ffa.utils

import net.minecraft.text.Text
import net.silkmc.silk.core.text.literalText

object ChatUtils {
    fun getProgressBar(
        current: Int,
        max: Int,
        totalBars: Int,
        symbol: Char,
        completedColor: Int = getProgressBarColor(current, max),
        notCompletedColor: Int = 0xa1a1a1
    ): Text {
        val percent = current.toFloat() / max
        val progressBars = (totalBars * percent).toInt()

        return literalText {
            repeat(progressBars) {
                text("" + symbol) {
                    color = completedColor
                }
            }
            repeat(totalBars - progressBars) {
                text("" + symbol) {
                    color = notCompletedColor
                }
            }
        }
    }

    fun getProgressBarColor(progress: Int, maxProgress: Int): Int {
        val percentage = progress.toDouble() / maxProgress * 100
        return when {
            percentage > 66 -> 0x6fff36
            percentage > 30 -> 0xfff700
            else -> 0xff0000
        }
    }
}
