package br.com.firstsoft.parlathon.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.firstsoft.parlathon.business.vo.Partido
import br.com.firstsoft.parlathon.util.loadImg
import kotlinx.android.synthetic.main.holder_partido.view.*

class PartidoViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {

    val partidoPicture = view.partidoPicture
    val partidoSigla = view.partidoSigla
    var partido: Partido? = null

    @SuppressLint("SetTextI18n")
    fun bind(partido: Partido) {
        this.partido = partido
        this.partidoPicture.loadImg(partido.foto)
        this.partidoSigla.text = partido.sigla
    }

}