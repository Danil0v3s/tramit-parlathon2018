package br.com.firstsoft.parlathon.business.vo

import java.io.Serializable

data class ProposicaoAutor(
        val uri: String,
        val nome: String,
        val codTipo: Int,
        val tipo: String
) : Serializable