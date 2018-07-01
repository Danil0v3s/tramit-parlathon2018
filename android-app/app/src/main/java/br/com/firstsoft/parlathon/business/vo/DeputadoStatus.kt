package br.com.firstsoft.parlathon.business.vo

import android.arch.persistence.room.Embedded
import java.io.Serializable

data class DeputadoStatus(
        val condicaoEleitoral: String,
        val data: String,
        val descricaoStatus: String,
        @Embedded
        val gabinete: Gabinete,
        val id: Int,
        val idLegislatura: Int,
        val nome: String,
        val nomeEleitoral: String,
        val siglaPartido: String,
        val siglaUf: String,
        val situacao: String,
        val uri: String,
        val uriPartido: String,
        val urlFoto: String
) : Serializable