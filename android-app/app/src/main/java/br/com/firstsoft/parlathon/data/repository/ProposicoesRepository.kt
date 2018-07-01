package br.com.firstsoft.parlathon.data.repository

import br.com.firstsoft.parlathon.business.vo.AppResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by danilolemes on 04/06/18.
 */
interface ProposicoesRepository {

    @GET("proposicoes")
    fun fetchProposicoes(@QueryMap params: HashMap<String, String> = HashMap()): Call<AppResponse>

    @GET("proposicoes/{proposicaoID}")
    fun detail(@Path("proposicaoID") proposicaoID: Int): Call<AppResponse>

    @GET("proposicoes/{proposicaoID}/autores")
    fun findAuthor(@Path("proposicaoID") proposicaoID: Int): Call<AppResponse>

    @GET("proposicoes/{proposicaoID}/tramitacoes")
    fun fetchTramitacoes(@Path("proposicaoID") proposicaoID: Int): Call<AppResponse>

    @GET("proposicoes/{proposicaoID}/relacionadas")
    fun fetchRelacionadas(@Path("proposicaoID") proposicaoID: Int): Call<AppResponse>

    @GET("referencias/tiposProposicao")
    fun fetchTiposProposicao(): Call<AppResponse>

    @GET("referencias/situacoesProposicao")
    fun fetchSituacoesProposicao(): Call<AppResponse>


}