package br.com.firstsoft.parlathon.view.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.business.model.ViewTypes
import br.com.firstsoft.parlathon.view.adapters.delegates.ProposicaoTimelineDelegateAdapter
import br.com.firstsoft.parlathon.view.adapters.delegates.ViewTypeDelegateAdapter

class ProposicaoTimelineAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(ViewTypes.TRAMITACAO, ProposicaoTimelineDelegateAdapter(R.layout.holder_timeline_proposicao, context))
        items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position], position, items.size)
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