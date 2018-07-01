package br.com.firstsoft.parlathon.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.firstsoft.parlathon.business.vo.DeputadoThumb
import br.com.firstsoft.parlathon.data.AppApplication.Companion.RepositoryComponent
import br.com.firstsoft.parlathon.data.service.DeputadosService
import javax.inject.Inject

/**
 * Created by danilolemes on 04/06/18.
 */
class DeputadosViewModel : ViewModel() {

    @Inject
    lateinit var deputadosService: DeputadosService

    init {
        RepositoryComponent.inject(this)
    }

    val deputadosThumb: MutableLiveData<List<DeputadoThumb>> = MutableLiveData()

    fun fetchDeputados(): LiveData<List<DeputadoThumb>> {

        deputadosService.fetchDeputados { deputados ->
            this.deputadosThumb.value = deputados
        }

        return deputadosThumb
    }

}