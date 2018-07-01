package br.com.firstsoft.parlathon.view.bottomsheet

import android.app.Dialog
import android.content.Intent
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.business.vo.ParlathonTramitacao
import br.com.firstsoft.parlathon.business.vo.TramitacaoProposicao
import br.com.firstsoft.parlathon.util.displayAsDate
import br.com.firstsoft.parlathon.util.gone
import br.com.firstsoft.parlathon.util.visible
import br.com.firstsoft.parlathon.view.activities.WebViewActivity
import kotlinx.android.synthetic.main.bottomsheet_despacho_proposicao.view.*

class DespachoBottomsheet : BottomSheetDialogFragment() {

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }


    override fun setupDialog(dialog: Dialog, style: Int) {

        val contentView = View.inflate(context, R.layout.bottomsheet_despacho_proposicao, null)
        dialog.setContentView(contentView)

        val layoutParams = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }

        val tramitacaoProposicao = arguments?.getSerializable(IntentFieldNames.TRAMITACAO_PROPOSICAO) as? ParlathonTramitacao


        if (tramitacaoProposicao != null) {
            contentView.btnClose.setOnClickListener { dismiss() }
            contentView.despachoDataText.text = tramitacaoProposicao.data.displayAsDate()
            contentView.despachoDescricaoText.text = tramitacaoProposicao.descricaoDaTramitacao
            contentView.despachoOrgaoText.text = tramitacaoProposicao.orgao.sigla
            if (tramitacaoProposicao.documento != null) {
                contentView.maisInfoBtn.visible()
                contentView.maisInfoBtn.setOnClickListener {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra(IntentFieldNames.WEBVIEW_LINK, tramitacaoProposicao.documento)
                    startActivity(intent)
                }
            } else {
                contentView.maisInfoBtn.gone()
            }
            contentView.despachoText.text = tramitacaoProposicao.despacho
        } else {
            dismiss()
        }


//        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))

    }

}