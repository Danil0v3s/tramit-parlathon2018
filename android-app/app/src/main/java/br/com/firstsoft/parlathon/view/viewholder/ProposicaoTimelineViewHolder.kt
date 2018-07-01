package br.com.firstsoft.parlathon.view.viewholder

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.vo.ParlathonCasaType
import br.com.firstsoft.parlathon.business.vo.ParlathonTramitacao
import br.com.firstsoft.parlathon.business.vo.TramitacaoProposicao
import br.com.firstsoft.parlathon.util.displayAsDate
import br.com.firstsoft.parlathon.util.gone
import br.com.firstsoft.parlathon.util.visible
import br.com.firstsoft.parlathon.view.bottomsheet.DespachoBottomsheet
import kotlinx.android.synthetic.main.holder_timeline_proposicao.view.*


class ProposicaoTimelineViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {

    val despachoTramitacao = view.despachoTramitacaoText
    val orgaoTramitacao = view.orgaoTramitacaoText
    val horaTramitacao = view.horaTramitacaoText
    val parentLayout = view.parentLayout
    val textWrapper = view.textWrapper
    val timelineIndicator = view.timelineIndicator
    val centerBottomLine = view.centerBottomLine
    val frame = view.frame

    var tramitacaoProposicao: ViewType? = null

    init {
        view.setOnClickListener(this)
    }

    fun bind(tramitacaoProposicao: ViewType, position: Int, listSize: Int) {
        this.tramitacaoProposicao = tramitacaoProposicao
        if (tramitacaoProposicao is TramitacaoProposicao) {
            bindTramitacaoProposicao(tramitacaoProposicao, position)
        } else if (tramitacaoProposicao is ParlathonTramitacao){
            this.despachoTramitacao.text = tramitacaoProposicao.descricaoDaTramitacao
            this.orgaoTramitacao.text = tramitacaoProposicao.orgao.sigla
            this.horaTramitacao.text = tramitacaoProposicao.data.displayAsDate()

            when (tramitacaoProposicao.casa.id) {
                ParlathonCasaType.CAMARA -> {
                    timelineIndicator.background = context.getDrawable(R.drawable.dot_camara_deputados)
                    centerBottomLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue))
                }
                ParlathonCasaType.SENADO -> {
                    timelineIndicator.background = context.getDrawable(R.drawable.dot_senado)
                    centerBottomLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange))
                }
                ParlathonCasaType.CONGRESSO -> {
                    timelineIndicator.background = context.getDrawable(R.drawable.dot_congresso)
                    centerBottomLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenDark))
                }
            }

            if (position == listSize -1) {
                centerBottomLine.gone()
            } else {
                centerBottomLine.visible()
            }
        }


        val params = frame.layoutParams as ConstraintLayout.LayoutParams
        val wrapperParams = textWrapper.layoutParams as FrameLayout.LayoutParams
        if (position % 2 != 0) {
            params.startToStart = ConstraintSet.PARENT_ID
            params.endToEnd = -1
            params.startToEnd = -1
            params.endToStart = timelineIndicator.id

            wrapperParams.gravity = Gravity.END
        } else {
            params.endToEnd = ConstraintSet.PARENT_ID
            params.endToStart = -1
            params.startToStart = -1
            params.startToEnd = timelineIndicator.id

            wrapperParams.gravity = Gravity.START
        }
    }

    private fun bindTramitacaoProposicao(tramitacaoProposicao: TramitacaoProposicao, position: Int) {
        this.despachoTramitacao.text = tramitacaoProposicao.descricaoTramitacao
        this.orgaoTramitacao.text = tramitacaoProposicao.siglaOrgao
        this.horaTramitacao.text = tramitacaoProposicao.dataHora.displayAsDate()

//        if (position == 0) {
//            centerTopLine.visibility = View.GONE
//        } else {
//            centerTopLine.visibility = View.VISIBLE
//        }

        if (tramitacaoProposicao.sequencia == 1) {
            centerBottomLine.visibility = View.GONE
        } else {
            centerBottomLine.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View?) {
        this.tramitacaoProposicao?.let {
            val bottomsheet = DespachoBottomsheet()
            val args = Bundle()
            args.putSerializable(IntentFieldNames.TRAMITACAO_PROPOSICAO, it)
            bottomsheet.arguments = args
            bottomsheet.show((context as AppCompatActivity).supportFragmentManager, "TRAMITACAO_BOTTOMSHEET")
        }
    }

}