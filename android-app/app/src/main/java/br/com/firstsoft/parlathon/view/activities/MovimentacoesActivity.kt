package br.com.firstsoft.parlathon.view.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.business.vo.ParlathonProposicao
import br.com.firstsoft.parlathon.data.AppApplication
import br.com.firstsoft.parlathon.view.adapters.ProposicaoTimelineAdapter
import br.com.firstsoft.parlathon.view.viewmodel.ProposicoesViewModel
import kotlinx.android.synthetic.main.activity_movimentacoes.*

class MovimentacoesActivity : AppCompatActivity() {

    private val proposicoesViewModel by lazy { ViewModelProviders.of(this).get(ProposicoesViewModel::class.java) }
    private val proposicoesTimelineAdapter by lazy { ProposicaoTimelineAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimentacoes)

        val proposicao = intent.extras.getSerializable(IntentFieldNames.PROPOSICAO) as ParlathonProposicao

        setupToolbar()
        setupViews(proposicao)

        proposicoesViewModel.fetchMovimentacoes(proposicao.id).observe(this, Observer { movimentacoes ->
            movimentacoes?.let {
                proposicoesTimelineAdapter.addAll(it)
                proposicaoTimelineRecycler.adapter = proposicoesTimelineAdapter
                proposicaoTimelineRecycler.layoutManager = LinearLayoutManager(this)
            }
        })
    }

    private fun setupViews(proposicao: ParlathonProposicao) {
        val tipo = AppApplication.tiposProposicao?.get(proposicao.siglaTipo)?.nome
        this.proposicaoName.text = "$tipo ${proposicao.numero}/${proposicao.ano}"
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.elevation = 0f
    }
}
