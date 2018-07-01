package br.com.firstsoft.parlathon.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.vo.Deputado
import br.com.firstsoft.parlathon.business.vo.DeputadoThumb
import br.com.firstsoft.parlathon.util.loadImg
import kotlinx.android.synthetic.main.holder_parlamentar.view.*

class ParlamentarViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {

    val deputadoPicture = view.parlamentarPicture
    val deputadoName = view.parlamentarName
    val deputadoPartido = view.parlamentarPartido
    var deputado: ViewType? = null

    @SuppressLint("SetTextI18n")
    fun bind(deputado: ViewType) {
        this.deputado = deputado

        if (deputado is DeputadoThumb) {
            this.deputadoPicture.loadImg(deputado.urlFoto)
            if (deputado.nome.split(" ").size > 1) {
                this.deputadoName.text = "${deputado.nome.split(" ").first().toLowerCase().capitalize()} ${deputado.nome.split(" ")[1].first()}."
            }
            if (deputado.siglaPartido != null) {
                this.deputadoPartido.text = "${deputado.siglaPartido} - ${deputado.siglaUf}"
            }
        } else if (deputado is Deputado) {
            this.deputadoPicture.loadImg(deputado.ultimoStatus?.urlFoto)
            if (deputado.nomeCivil.split(" ").size > 1) {
                this.deputadoName.text = "${deputado.nomeCivil.split(" ").first().toLowerCase().capitalize()} ${deputado.nomeCivil.split(" ")[1].first()}."
            }
            this.deputadoPartido.text = "${deputado.ultimoStatus?.siglaPartido} - ${deputado.ultimoStatus?.siglaUf}"
        }


    }

}