package br.com.firstsoft.parlathon.business.vo

import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.model.ViewTypes
import java.io.Serializable

data class ParlathonTramitacao(val casa: ParlathonCasa,
                               val data: String,
                               val orgao: Orgao,
                               val descricaoDaTramitacao: String,
                               val descricaoDaSituacao: String?,
                               val despacho: String,
                               val documento: String?) : Serializable, ViewType {

    override fun getViewType(): Int {
        return ViewTypes.TRAMITACAO
    }

}

data class Orgao(val sigla: String, val nome: String) : Serializable

data class ParlathonProposicao(val id: Int,
                               val origem: String,
                               val siglaTipo: String,
                               val numero: Int,
                               val ano: Int,
                               val ementa: String,
                               val situacao: String,
                               val casa: ParlathonCasa,
                               val autor: ParlathonAutor,
                               val dataApresentacao: String
) : Serializable, ViewType {
    override fun getViewType(): Int {
        return ViewTypes.PROPOSICAO
    }
}

data class DetalheParlathonProposicao(val id: Int,
                                      val origem: String,
                                      val assunto: String,
                                      val siglaTipo: String,
                                      val descricaoTipo: String,
                                      val numero: Int,
                                      val ano: Int,
                                      val dataApresentacao: String,
                                      val regime: String,
                                      val situacao: String,
                                      val tramitando: Boolean,
                                      val ementa: String,
                                      val explicacaoEmenta: String,
                                      val autores: List<ParlathonAutor>,
                                      val transformadoEm: ProposicaoObjetoFinal,
                                      val proposicoesRelacionadas: List<ParlathonProposicao>,
                                      val linhaDoTempo: ParlathonTimeline,
                                      val url: String,
                                      val casa: ParlathonCasa) : Serializable, ViewType {
    override fun getViewType(): Int {
        return ViewTypes.PROPOSICAO
    }
}

data class ProposicaoObjetoFinal(val id: String,
                                 val tipo: String,
                                 val numero: String,
                                 val ano: String,
                                 val data: String)

data class ParlathonTimeline(val casaIniciadora: String?,
                             val casaRevisora: String?,
                             val sancaoPresidencial: String?,
                             val promulgacaoEPublicacao: String?) : Serializable

data class ParlathonAutor(
        val id: String,
        val nome: String,
        val tipo: String,
        val partido: String,
        val foto: String
) : Serializable

data class ParlathonCasa(val id: Int, val nome: String) : Serializable

object ParlathonCasaType {
    val CAMARA = 1
    val SENADO = 2
    val CONGRESSO = 3
    val CAMARA_DPT = "Câmara dos Deputados"
    val SENADO_FED = "Senado Federal"
}