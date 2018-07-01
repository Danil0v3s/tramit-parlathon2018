package br.com.firstsoft.parlathon.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.firstsoft.parlathon.business.vo.Partido
import br.com.firstsoft.parlathon.data.AppApplication.Companion.RepositoryComponent
import br.com.firstsoft.parlathon.data.service.PartidosService
import javax.inject.Inject

class PartidosViewModel : ViewModel() {

    @Inject
    lateinit var partidosService: PartidosService

    init {
        RepositoryComponent.inject(this)
    }

    val partidos: MutableLiveData<List<Partido>> = MutableLiveData()

    fun fetchPartidos(): LiveData<List<Partido>> {
        partidosService.fetchPartidos { partidos ->
            this.partidos.value = partidos
        }

        return partidos
    }
}