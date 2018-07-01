package br.com.firstsoft.parlathon.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.util.inflate
import br.com.firstsoft.parlathon.view.components.GlideApp
import kotlinx.android.synthetic.main.fragment_tramit_info.*


class TramitInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.inflate(R.layout.fragment_tramit_info)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resID = arguments?.getInt(IntentFieldNames.RES_ID) ?: 0
        GlideApp.with(this).load(resID).into(image)
    }

}