package br.com.firstsoft.parlathon.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.business.vo.ParlathonProposicao
import br.com.firstsoft.parlathon.util.loadImg
import br.com.firstsoft.parlathon.view.activities.ProposicaoDetailActivity
import kotlinx.android.synthetic.main.holder_proposicao.view.*

class ProposicaoViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val deputadoPicture = view.deputadoPicture
    val proposicaoName = view.proposicaoName
    val proposicaoTipo = view.proposicaoTipo
    val nomeDeputadoText = view.nomeDeputadoText
    val statusProposicaoText = view.statusProposicaoText
    val ementaProposicaoText = view.ementaProposicaoText

    init {
        view.setOnClickListener(this)
    }

    var proposicao: ParlathonProposicao? = null

    @SuppressLint("SetTextI18n")
    fun bind(proposicao: ParlathonProposicao) {
        this.proposicao = proposicao
        this.proposicaoName.text = "${proposicao.numero}/${proposicao.ano}"
        this.statusProposicaoText.text = proposicao.situacao
        this.ementaProposicaoText.text = proposicao.ementa
        this.proposicaoTipo.text = proposicao.siglaTipo
        this.deputadoPicture.loadImg(proposicao.autor.foto)
        this.nomeDeputadoText.text = proposicao.autor.nome
    }

    override fun onClick(p0: View?) {
        this.proposicao?.let {
            val intent = Intent(context, ProposicaoDetailActivity::class.java)
            intent.putExtra(IntentFieldNames.PROPOSICAO, it)
            context.startActivity(intent)
        }
    }

}