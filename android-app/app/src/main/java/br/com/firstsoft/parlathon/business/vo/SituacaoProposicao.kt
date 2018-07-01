package br.com.firstsoft.parlathon.business.vo

import java.io.Serializable

data class SituacaoProposicao(
        val id: String,
        val sigla: String,
        val nome: String,
        val descricao: String
) : Serializable