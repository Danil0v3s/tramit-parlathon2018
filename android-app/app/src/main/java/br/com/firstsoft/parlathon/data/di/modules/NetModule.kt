package br.com.firstsoft.parlathon.data.di.modules

import android.app.Application
import android.content.SharedPreferences
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.data.AppConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(application.getString(R.string.app_name), 0)
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, sharedPreferences: SharedPreferences, context: Application, httpLogingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addNetworkInterceptor(httpLogingInterceptor)
        client.cache(cache)
        client.readTimeout(2, TimeUnit.MINUTES)
        client.connectTimeout(2, TimeUnit.MINUTES)
        client.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .build()

            val requestBuilder = original.newBuilder()
                    .addHeader("accept", "application/json")
//                    .addHeader("x-api-key", context.getString(R.string.x_app_key))
                    .url(url)

            val cookie = sharedPreferences.getString(context.getString(R.string.cookie), "")
            if (cookie.isNotEmpty()) {
                requestBuilder.addHeader(context.getString(R.string.cookie), cookie)
            }

            val request = requestBuilder.build()
            val response = chain.proceed(request)

            response
        }
        return client.build()
    }

    @Provides
    @Singleton
    @Named("DADOS_ABERTOS_CAMARA")
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(AppConstants.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @Singleton
    @Provides
    @Named("PARLATHON_FUNCTIONS")
    fun providesRetrofitParlathon(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(AppConstants.PARLATHON_FUNCTIONS_URL)
                .client(okHttpClient)
                .build()
    }


}