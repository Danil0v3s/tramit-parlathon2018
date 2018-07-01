package br.com.firstsoft.parlathon.data.repository

import br.com.firstsoft.parlathon.business.vo.AppResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface DeputadosRepository {

    @GET("deputados")
    fun fetchDeputados(@QueryMap params: HashMap<String, String> = HashMap()): Call<AppResponse>

    @GET("deputados/{idDeputado}")
    fun detail(@Path("idDeputado") deputadoID: Int): Call<AppResponse>

}