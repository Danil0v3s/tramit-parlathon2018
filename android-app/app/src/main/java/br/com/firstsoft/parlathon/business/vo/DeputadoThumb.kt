package br.com.firstsoft.parlathon.business.vo

import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.model.ViewTypes
import java.io.Serializable

data class DeputadoThumb(
        val id: Int?,
        val uri: String?,
        val nome: String,
        val siglaPartido: String?,
        val uriPartido: String?,
        val siglaUf: String?,
        val idLegislatura: Int?,
        val urlFoto: String?) : Serializable, ViewType {

    companion object {
        fun placeholder() : DeputadoThumb {
            return DeputadoThumb(null, null, "", null, null, null, null, null)
        }
    }

    override fun getViewType(): Int = ViewTypes.DEPUTADO
}