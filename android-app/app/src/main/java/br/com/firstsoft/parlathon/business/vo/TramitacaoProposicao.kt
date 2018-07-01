package br.com.firstsoft.parlathon.business.vo

import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.model.ViewTypes
import java.io.Serializable

data class TramitacaoProposicao(
    val dataHora: String,
    val sequencia: Int,
    val siglaOrgao: String,
    val uriOrgao: String,
    val regime: String,
    val descricaoTramitacao: String,
    val idTipoTramitacao: String,
    val descricaoSituacao: String?,
    val idSituacao: String?,
    val despacho: String,
    val url: String?
) : Serializable, ViewType {

    override fun getViewType(): Int = ViewTypes.TRAMITACAO

}