package br.com.firstsoft.parlathon.business.vo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import br.com.firstsoft.parlathon.business.model.ViewTypes
import br.com.firstsoft.parlathon.business.model.ViewType
import java.io.Serializable

@Entity(tableName = "partidos")
class Partido @Ignore constructor() : Serializable, ViewType {

    override fun getViewType(): Int {
        return ViewTypes.PARTIDO
    }

    @NonNull
    @PrimaryKey
    var id: Int = -1
    var foto: String? = ""
    var sigla: String = ""
    var nome: String = ""

    constructor(id: Int, uri: String?, sigla: String, nome: String) : this() {
        this.id = id
        this.foto = uri
        this.sigla = sigla
        this.nome = nome
    }
}