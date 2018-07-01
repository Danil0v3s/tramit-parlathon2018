package br.com.firstsoft.parlathon.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.firstsoft.parlathon.business.vo.*
import br.com.firstsoft.parlathon.data.AppApplication.Companion.RepositoryComponent
import br.com.firstsoft.parlathon.data.service.ProposicoesService
import javax.inject.Inject

/**
 * Created by danilolemes on 04/06/18.
 */
class ProposicoesViewModel : ViewModel() {

    @Inject
    lateinit var proposicoesService: ProposicoesService

    init {
        RepositoryComponent.inject(this)
    }

    val tramitacoes: MutableLiveData<MutableList<TramitacaoProposicao>> = MutableLiveData()
    val movimentacoes: MutableLiveData<MutableList<ParlathonTramitacao>> = MutableLiveData()
    val projetosSenado: MutableLiveData<List<ParlathonProposicao>> = MutableLiveData()
    val projetosCamara: MutableLiveData<List<ParlathonProposicao>> = MutableLiveData()
    val resultadoPesquisa: MutableLiveData<List<ParlathonProposicao>> = MutableLiveData()

    fun fetchTramitacoes(proposicaoID: Int): LiveData<MutableList<TramitacaoProposicao>> {
        proposicoesService.fetchTramitacoes(proposicaoID) { tramitacoes ->
            tramitacoes?.let {
                it as MutableList<TramitacaoProposicao>
                it.sortByDescending { it.sequencia }
                this.tramitacoes.value = it
            }
        }

        return tramitacoes
    }

    fun fetchMovimentacoes(proposicaoID: Int): LiveData<MutableList<ParlathonTramitacao>> {
        proposicoesService.fetchMovimentacoes(proposicaoID) { movimentacoes ->
            movimentacoes?.let {
                it as MutableList<ParlathonTramitacao>
                this.movimentacoes.value = it
            }
        }

        return movimentacoes
    }

    fun fetchTiposProposicao(listener: (Map<String, TipoProposicao>?) -> Unit) {
        proposicoesService.fetchTiposProposicao { listener(it) }
    }

    fun fetchSituacoesProposicao(listener: (Map<String, SituacaoProposicao>?) -> Unit) {
        proposicoesService.fetchSituacoesProposicao { listener(it) }
    }

    fun fetchProjetosCamara(): LiveData<List<ParlathonProposicao>> {
        proposicoesService.fetchProjetosCamara { projetos ->
            projetos?.let {
                projetosCamara.value = it
            }
        }

        return projetosCamara
    }

    fun fetchProjetosSenado(): LiveData<List<ParlathonProposicao>> {
        proposicoesService.fetchProjetosSenado { projetos ->
            projetos?.let {
                projetosSenado.value = it
            }
        }

        return projetosSenado
    }

    fun fetchDetalhesSenado(proposicaoID: String, listener: (DetalheParlathonProposicao?) -> Unit) {
        proposicoesService.fetchDetalhesSenado(proposicaoID, listener)
    }

    fun fetchDetalhesCamara(proposicaoID: String, listener: (DetalheParlathonProposicao?) -> Unit) {
        proposicoesService.fetchDetalhesCamara(proposicaoID, listener)
    }

    fun fetchPesquisa(params: HashMap<String, String>): LiveData<List<ParlathonProposicao>> {
        proposicoesService.fetchPesquisa(params) { result ->
            resultadoPesquisa.value = result
        }

        return resultadoPesquisa
    }

}