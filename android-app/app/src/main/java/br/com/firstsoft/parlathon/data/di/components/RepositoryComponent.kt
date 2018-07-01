package br.com.firstsoft.parlathon.data.di.components

import android.content.SharedPreferences
import br.com.firstsoft.parlathon.data.di.modules.RepositoryModule
import br.com.firstsoft.parlathon.data.di.scopes.UserScope
import br.com.firstsoft.parlathon.data.service.DeputadosService
import br.com.firstsoft.parlathon.data.service.PartidosService
import br.com.firstsoft.parlathon.data.service.ProposicoesService
import br.com.firstsoft.parlathon.view.viewmodel.DeputadosViewModel
import br.com.firstsoft.parlathon.view.viewmodel.PartidosViewModel
import br.com.firstsoft.parlathon.view.viewmodel.ProposicoesViewModel
import com.google.gson.Gson
import dagger.Component


@UserScope
@Component(dependencies = [NetComponent::class], modules = [RepositoryModule::class])
interface RepositoryComponent {

    fun sharedPreferences(): SharedPreferences
    fun gson(): Gson

    fun inject(proposicoesService: ProposicoesService)
    fun inject(deputadosService: DeputadosService)
    fun inject(partidosService: PartidosService)

    fun inject(proposicoesViewModel: ProposicoesViewModel)
    fun inject(deputadosViewModel: DeputadosViewModel)
    fun inject(partidosViewModel: PartidosViewModel)
}
