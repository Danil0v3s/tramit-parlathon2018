@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package br.com.firstsoft.parlathon.data.service

import br.com.firstsoft.parlathon.business.vo.Partido
import br.com.firstsoft.parlathon.data.AppApplication
import br.com.firstsoft.parlathon.data.repository.PartidosRepository
import br.com.firstsoft.parlathon.data.util.DataParser
import br.com.firstsoft.parlathon.data.util.HttpUtils.Companion.parseResponse
import javax.inject.Inject

class PartidosService {

    @Inject
    lateinit var partidosRepository: PartidosRepository

    @Inject
    lateinit var dataParser: DataParser

    init {
        AppApplication.RepositoryComponent.inject(this)
    }

    fun fetchPartidos(listener: (List<Partido>?) -> Unit) {
        partidosRepository.fetchPartidos().enqueue(parseResponse { appResponse, throwable ->
            throwable?.let {
                listener(null)
                it.printStackTrace()
            }
            appResponse?.let {
                val data = dataParser.parseList(it.dados as List<*>, Partido::class.java)
                listener(data)
            }
        })
    }
}