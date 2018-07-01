package br.com.firstsoft.parlathon.business.vo

import java.io.Serializable

/**
 * Created by danilolemes on 04/06/18.
 */
data class StatusProposicao(
        var dataHora: String? = null,
        var sequencia: String? = null,
        var siglaOrgao: String? = null,
        var uriOrgao: String? = null,
        var regime: String? = null,
        var descricaoTramitacao: String? = null,
        var idTipoTramitacao: Int = -1,
        var descricaoSituacao: String? = null,
        var idSituacao: Int? = null,
        var despacho: String? = null,
        var url: String = ""
): Serializable