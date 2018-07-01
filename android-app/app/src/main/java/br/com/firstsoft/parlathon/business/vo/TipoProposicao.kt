package br.com.firstsoft.parlathon.business.vo

import java.io.Serializable

//"id": "129",
//"sigla": "CON",
//"nome": "Consulta",
//"descricao": "Consulta"

data class TipoProposicao(
        val id: String,
        val sigla: String,
        val nome: String,
        val descricao: String
) : Serializable