package br.com.firstsoft.parlathon.view.adapters.delegates

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.vo.ParlathonProposicao
import br.com.firstsoft.parlathon.util.inflate
import br.com.firstsoft.parlathon.view.viewholder.ProposicaoViewHolder

class ProposicaoDelegateAdapter(val resID: Int, val context: Context) : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ProposicaoViewHolder(parent.inflate(resID), context)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int, listSize: Int) {
        holder as ProposicaoViewHolder
        holder.bind(item as ParlathonProposicao)
    }

}