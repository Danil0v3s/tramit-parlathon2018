package br.com.firstsoft.parlathon.data.di.components

import android.content.SharedPreferences
import br.com.firstsoft.parlathon.data.di.modules.AppModule
import br.com.firstsoft.parlathon.data.di.modules.NetModule
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface NetComponent {

    fun gson(): Gson
    @Named("DADOS_ABERTOS_CAMARA")
    fun retrofit(): Retrofit
    @Named("PARLATHON_FUNCTIONS")
    fun retrofitParlathon(): Retrofit
    fun okHttpClient(): OkHttpClient
    fun sharedPreferences(): SharedPreferences

}