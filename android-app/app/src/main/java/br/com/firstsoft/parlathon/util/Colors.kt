package br.com.firstsoft.parlathon.util

import br.com.firstsoft.parlathon.R

object Gradients {
    val cyan = Pair(R.color.gradientCyanStart, R.color.gradientCyanEnd)
    val blue = Pair(R.color.gradientBlueStart, R.color.gradientBlueEnd)
    val deepBlue = Pair(R.color.gradientDeepBlueStart, R.color.gradientDeepBlueEnd)
    val purple = Pair(R.color.gradientPurpleStart, R.color.gradientPurpleEnd)
    val yellow = Pair(R.color.gradientYellowStart, R.color.gradientYellowEnd)
    val orange = Pair(R.color.gradientOrangeStart, R.color.gradientOrangeEnd)
    val lime = Pair(R.color.gradientLimeStart, R.color.gradientLimeEnd)
    val green = Pair(R.color.gradientGreenStart, R.color.gradientGreenEnd)
    val red = Pair(R.color.gradientRedStart, R.color.gradientRedEnd)
    val pink = Pair(R.color.gradientPinkStart, R.color.gradientPinkEnd)
}

class Colors() {
    fun getColorsFromPartido(sigla : String): Pair<Int, Int> {
        return when (sigla) {
            "AVANTE" -> Gradients.orange
            "DC" -> Gradients.blue
            "DEM" -> Gradients.lime
            "MDB" -> Gradients.green
            "NOVO" -> Gradients.orange
            "PATRI" -> Gradients.green
            "PCB" -> Gradients.red
            "PCdoB" -> Gradients.red
            "PCO" -> Gradients.red
            "PDT" -> Gradients.orange
            "PHS" -> Gradients.orange
            "PMB" -> Gradients.deepBlue
            "PMN" -> Gradients.red
            "PODE" -> Gradients.yellow
            "PP" -> Gradients.orange
            "PPL" -> Gradients.green
            "PPS" -> Gradients.pink
            "PR" -> Gradients.deepBlue
            "PRB" -> Gradients.cyan
            "PROS" -> Gradients.orange
            "PRP" -> Gradients.cyan
            "PRTB" -> Gradients.green
            "PSB" -> Gradients.yellow
            "PSC" -> Gradients.green
            "PSD" -> Gradients.lime
            "PSDB" -> Gradients.blue
            "PSL" -> Gradients.purple
            "PSOL" -> Gradients.red
            "PSTU" -> Gradients.red
            "PT" -> Gradients.red
            "PTB" -> Gradients.cyan
            "PTC" -> Gradients.blue
            "PV" -> Gradients.green
            "REDE" -> Gradients.cyan
            "SD" -> Gradients.orange
            else -> Gradients.blue
        }
    }
}
