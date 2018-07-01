package br.com.firstsoft.parlathon.view.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.model.ViewTypes
import br.com.firstsoft.parlathon.view.adapters.delegates.ProposicaoDelegateAdapter
import br.com.firstsoft.parlathon.view.adapters.delegates.ViewTypeDelegateAdapter
import br.com.firstsoft.parlathon.view.viewholder.ProposicaoViewHolder

class ProposicaoAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(ViewTypes.PROPOSICAO, ProposicaoDelegateAdapter(R.layout.holder_proposicao, context))
        items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position], position)
        if (holder is ProposicaoViewHolder) {
            ViewCompat.setTransitionName(holder.deputadoPicture, "view_$position")
        }
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun getItemCount(): Int = items.size

    fun addAll(items: List<ViewType>) {
//        val initPosition = items.size - 1
//        this.items.addAll(0, items)
//        notifyItemRangeChanged(initPosition, items.size + this.items.size)
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun add(item: ViewType) {
        val initPosition = items.size - 1
        items.add(0, item)
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }


}