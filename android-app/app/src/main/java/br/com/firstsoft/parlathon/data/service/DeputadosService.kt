@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.firstsoft.parlathon.data.service

import br.com.firstsoft.parlathon.business.vo.DeputadoThumb
import br.com.firstsoft.parlathon.data.AppApplication
import br.com.firstsoft.parlathon.data.repository.DeputadosRepository
import br.com.firstsoft.parlathon.data.util.DataParser
import br.com.firstsoft.parlathon.data.util.HttpUtils.Companion.parseResponse
import javax.inject.Inject

class DeputadosService {

    @Inject
    lateinit var deputadosRepository: DeputadosRepository

    @Inject
    lateinit var dataParser: DataParser

    init {
        AppApplication.RepositoryComponent.inject(this)
    }

    fun fetchDeputados(listener: (List<DeputadoThumb>?) -> Unit) {
        deputadosRepository.fetchDeputados().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let {
                listener(null)
                it.printStackTrace()
            }
            appResponse?.let {
                val data = dataParser.parseList(it.dados as List<*>, DeputadoThumb::class.java)
                listener(data)
            }
        })
    }

}