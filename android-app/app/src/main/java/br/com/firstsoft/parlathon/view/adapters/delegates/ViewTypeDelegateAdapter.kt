package br.com.firstsoft.parlathon.view.adapters.delegates

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import br.com.firstsoft.parlathon.business.model.ViewType

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int = 0, listSize: Int = 0)
}