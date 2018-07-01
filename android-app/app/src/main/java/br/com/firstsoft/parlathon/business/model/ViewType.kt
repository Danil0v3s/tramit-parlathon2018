package br.com.firstsoft.parlathon.business.model

import java.io.Serializable

interface ViewType : Serializable {
    fun getViewType(): Int
}

object ViewTypes {
    val PROPOSICAO = 1
    val DEPUTADO = 2
    val TRAMITACAO = 3
    val PARTIDO = 4
}