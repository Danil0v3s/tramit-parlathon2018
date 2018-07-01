package br.com.firstsoft.parlathon.data.repository

import br.com.firstsoft.parlathon.business.vo.DetalheParlathonProposicao
import br.com.firstsoft.parlathon.business.vo.ParlathonProposicao
import br.com.firstsoft.parlathon.business.vo.ParlathonTramitacao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ParlathonRepository {

    @GET("movimentacoes")
    fun fetchMovimentacoes(@Query("proposicaoId") proposicaoID: Int): Call<List<ParlathonTramitacao>>

    @GET("projetos-de-lei-da-camara")
    fun fetchProjetosCamara(): Call<List<ParlathonProposicao>>

    @GET("projetos-de-lei-do-senado")
    fun fetchProjetosSenado(): Call<List<ParlathonProposicao>>

    @GET("projeto-de-lei-do-senado")
    fun fetchDetalhesProjetoSenado(@Query("proposicaoId") proposicaoID: String): Call<DetalheParlathonProposicao>

    @GET("projeto-de-lei-da-camara")
    fun fetchDetalhesProjetoCamara(@Query("proposicaoId") proposicaoID: String): Call<DetalheParlathonProposicao>

    @GET("pesquisar")
    fun fetchPesquisa(@QueryMap params: HashMap<String, String>): Call<List<ParlathonProposicao>>

}