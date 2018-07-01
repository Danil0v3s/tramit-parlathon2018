@file:Suppress("IMPLICIT_CAST_TO_ANY")

package br.com.firstsoft.parlathon.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.business.model.ListType
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.view.adapters.ParlamentarAdapter
import br.com.firstsoft.parlathon.view.adapters.ProposicaoAdapter
import kotlinx.android.synthetic.main.activity_view_more.*
import kotlinx.android.synthetic.main.toolbar_clear.*

class ViewMoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)

        setupToolbar()

        val listType = intent.extras.getSerializable(IntentFieldNames.LIST_TYPE) as ListType
        val firstVisibleItem = intent.extras.getSerializable(IntentFieldNames.FIRST_VISIBLE_ITEM) as? Int
                ?: 0
        val items = intent.extras.getSerializable(IntentFieldNames.ITEMS) as List<ViewType>

        supportPostponeEnterTransition()


        lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
        lateinit var layoutManager: RecyclerView.LayoutManager
        when (listType) {
            ListType.PROPOSICAO -> {
                layoutManager = LinearLayoutManager(this)
                adapter = ProposicaoAdapter(this)
                adapter.clear()
                adapter.addAll(items)
            }
            ListType.DEPUTADO -> {
                layoutManager = GridLayoutManager(this,3)
                adapter = ParlamentarAdapter(this)
                adapter.clear()
                adapter.addAll(items)
            }
        }

        viewMoreRecycler.adapter = adapter
        viewMoreRecycler.layoutManager = layoutManager
        viewMoreRecycler.scrollToPosition(firstVisibleItem)
        viewMoreRecycler.post { supportStartPostponedEnterTransition() }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
