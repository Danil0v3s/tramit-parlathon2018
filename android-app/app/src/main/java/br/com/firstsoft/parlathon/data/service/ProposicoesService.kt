@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.firstsoft.parlathon.data.service

import br.com.firstsoft.parlathon.business.vo.*
import br.com.firstsoft.parlathon.data.AppApplication
import br.com.firstsoft.parlathon.data.repository.DeputadosRepository
import br.com.firstsoft.parlathon.data.repository.ParlathonRepository
import br.com.firstsoft.parlathon.data.repository.ProposicoesRepository
import br.com.firstsoft.parlathon.data.util.DataParser
import br.com.firstsoft.parlathon.data.util.HttpUtils.Companion.callback
import br.com.firstsoft.parlathon.data.util.HttpUtils.Companion.parseResponse
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

/**
 * Created by danilolemes on 04/06/18.
 */
class ProposicoesService {

    @Inject
    lateinit var proposicoesRepository: ProposicoesRepository

    @Inject
    lateinit var deputadosRepository: DeputadosRepository

    @Inject
    lateinit var parlathonRepository: ParlathonRepository

    @Inject
    lateinit var dataParser: DataParser

    init {
        AppApplication.RepositoryComponent.inject(this)
    }

    fun findAuthor(proposicaoID: Int, listener: (List<Deputado>?) -> Unit) {
        proposicoesRepository.findAuthor(proposicaoID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                val data = dataParser.parseList(it.dados as List<*>, DeputadoThumb::class.java)
                detailAuthor(data, listener)
            }
        })
    }

    fun detailAuthor(data: List<DeputadoThumb>, listener: (List<Deputado>?) -> Unit) = async(UI) {
        val dpts = mutableListOf<Deputado>()
        if (data.any { it.uri != null }) {
            data.filter { it.uri != null }.forEach { depThumb ->
                val deputadoID = depThumb.uri!!.split('/').last().toInt()

                val job = async(CommonPool) {
                    deputadosRepository.detail(deputadoID).execute()
                }
                val appResponse = job.await().body()
                appResponse?.let {
                    val dpt = dataParser.parseObject(it.dados, Deputado::class.java)
                    dpts.add(dpt)
                }
            }
            listener(dpts)
        } else {
            listener(null)
        }
    }

    fun fetchTramitacoes(proposicaoID: Int, listener: (List<TramitacaoProposicao>?) -> Unit) {
        proposicoesRepository.fetchTramitacoes(proposicaoID).enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                val data = dataParser.parseList(it.dados as List<*>, TramitacaoProposicao::class.java)
                listener(data)
            }
        })
    }

    fun fetchTiposProposicao(listener: (Map<String, TipoProposicao>?) -> Unit) {
        proposicoesRepository.fetchTiposProposicao().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                val data = dataParser.parseList(it.dados as List<*>, TipoProposicao::class.java)
                listener(data.map { it.sigla to it }.toMap())
            }
        })
    }

    fun fetchSituacoesProposicao(listener: (Map<String, SituacaoProposicao>?) -> Unit) {
        proposicoesRepository.fetchSituacoesProposicao().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let { listener(null); it.printStackTrace() }
            appResponse?.let {
                val data = dataParser.parseList(it.dados as List<*>, SituacaoProposicao::class.java)
                listener(data.map { it.id to it }.toMap())
            }
        })
    }

    fun fetchMovimentacoes(proposicaoID: Int, listener: (List<ParlathonTramitacao>?) -> Unit) {
        parlathonRepository.fetchMovimentacoes(proposicaoID).enqueue(callback(
                { response -> listener(response.body()) },
                { throwable -> listener(null); throwable.printStackTrace() }
        ))
    }

    fun fetchProjetosCamara(listener: (List<ParlathonProposicao>?) -> Unit) {
        parlathonRepository.fetchProjetosCamara().enqueue(callback(
                { response -> listener(response.body()) },
                { throwable -> listener(null); throwable.printStackTrace() }
        ))
    }

    fun fetchProjetosSenado(listener: (List<ParlathonProposicao>?) -> Unit) {
        parlathonRepository.fetchProjetosSenado().enqueue(callback(
                { response -> listener(response.body()) },
                { throwable -> listener(null); throwable.printStackTrace() }
        ))
    }

    fun fetchDetalhesSenado(proposicaoID: String, listener: (DetalheParlathonProposicao?) -> Unit) {
        parlathonRepository.fetchDetalhesProjetoSenado(proposicaoID).enqueue(callback(
                { response -> listener(response.body()) },
                { throwable -> listener(null); throwable.printStackTrace() }
        ))
    }

    fun fetchDetalhesCamara(proposicaoID: String, listener: (DetalheParlathonProposicao?) -> Unit) {
        parlathonRepository.fetchDetalhesProjetoCamara(proposicaoID).enqueue(callback(
                { response -> listener(response.body()) },
                { throwable -> listener(null); throwable.printStackTrace() }
        ))
    }

    fun fetchPesquisa(params: HashMap<String, String>, listener: (List<ParlathonProposicao>?) -> Unit) {
        parlathonRepository.fetchPesquisa(params).enqueue(callback(
                { response -> listener(response.body()) },
                { throwable -> listener(null) }
        ))
    }

}