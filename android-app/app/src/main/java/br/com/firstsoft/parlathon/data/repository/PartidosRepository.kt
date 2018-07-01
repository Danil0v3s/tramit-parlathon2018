package br.com.firstsoft.parlathon.data.repository

import br.com.firstsoft.parlathon.business.vo.AppResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface PartidosRepository {

    @GET("partidos")
    fun fetchPartidos(@QueryMap params: HashMap<String, String> = HashMap()): Call<AppResponse>

    @GET("partidos/{partidoID}")
    fun detail(@Path("partidoID") partidoID: Int): Call<AppResponse>

}