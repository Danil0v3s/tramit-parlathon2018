package br.com.firstsoft.parlathon.data.di.modules

import br.com.firstsoft.parlathon.data.db.impl.AppDatabaseImpl
import br.com.firstsoft.parlathon.data.di.scopes.UserScope
import br.com.firstsoft.parlathon.data.repository.DeputadosRepository
import br.com.firstsoft.parlathon.data.repository.ParlathonRepository
import br.com.firstsoft.parlathon.data.repository.PartidosRepository
import br.com.firstsoft.parlathon.data.repository.ProposicoesRepository
import br.com.firstsoft.parlathon.data.service.DeputadosService
import br.com.firstsoft.parlathon.data.service.PartidosService
import br.com.firstsoft.parlathon.data.service.ProposicoesService
import br.com.firstsoft.parlathon.data.util.DataParser
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class RepositoryModule {

    @Provides
    @UserScope
    fun providesProposicoesRepository(@Named("DADOS_ABERTOS_CAMARA") retrofit: Retrofit) = retrofit.create(ProposicoesRepository::class.java)

    @Provides
    @UserScope
    fun providesDeputadosRepository(@Named("DADOS_ABERTOS_CAMARA") retrofit: Retrofit) = retrofit.create(DeputadosRepository::class.java)

    @Provides
    @UserScope
    fun providesPartidosRepository(@Named("PARLATHON_FUNCTIONS") retrofit: Retrofit) = retrofit.create(PartidosRepository::class.java)

    @Provides
    @UserScope
    fun providesParlathonRepository(@Named("PARLATHON_FUNCTIONS") retrofit: Retrofit) = retrofit.create(ParlathonRepository::class.java)

    @Provides
    @UserScope
    fun providesProposicoesService() = ProposicoesService()

    @Provides
    @UserScope
    fun providesDeputadosService() = DeputadosService()

    @Provides
    @UserScope
    fun providesPartidosService() = PartidosService()

    @Provides
    @UserScope
    fun providesDataParser(gson: Gson) = DataParser(gson)

}