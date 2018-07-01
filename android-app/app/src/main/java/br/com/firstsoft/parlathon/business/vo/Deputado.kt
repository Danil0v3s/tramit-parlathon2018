package br.com.firstsoft.parlathon.business.vo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.model.ViewTypes
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity
class Deputado @Ignore constructor() : Serializable, ViewType {

    override fun getViewType(): Int {
        return ViewTypes.DEPUTADO
    }

    @PrimaryKey
    @NotNull
    var id: Int = -1
    var cpf: String = ""
    var dataFalecimento: String = ""
    var dataNascimento: String = ""
    var escolaridade: String = ""
    var municipioNascimento: String = ""
    var nomeCivil: String = ""
    @Ignore
    var redeSocial: List<String> = listOf()
    var sexo: String = ""
    var ufNascimento: String = ""
    @Embedded
    var ultimoStatus: DeputadoStatus? = null
    var uri: String = ""
    var urlWebsite: String = ""

    constructor(id: Int, cpf: String, dataFalecimento: String,
                dataNascimento: String, escolaridade: String, municipioNascimento: String,
                nomeCivil: String, sexo: String, ufNascimento: String,
                ultimoStatus: DeputadoStatus, uri: String, urlWebsite: String) : this() {
        this.id = id
        this.cpf = cpf
        this.dataFalecimento = dataFalecimento
        this.dataNascimento = dataNascimento
        this.escolaridade = escolaridade
        this.municipioNascimento = municipioNascimento
        this.nomeCivil = nomeCivil
        this.sexo = sexo
        this.ufNascimento = ufNascimento
        this.ultimoStatus = ultimoStatus
        this.uri = uri
        this.urlWebsite = urlWebsite
    }

}