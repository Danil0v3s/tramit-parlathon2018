package br.com.firstsoft.parlathon.view.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.util.gone
import br.com.firstsoft.parlathon.util.visible
import br.com.firstsoft.parlathon.view.adapters.ProposicaoAdapter
import br.com.firstsoft.parlathon.view.viewmodel.ProposicoesViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_pesquisar_card.*

class SearchActivity : AppCompatActivity() {

    private val proposicoesViewModel by lazy { ViewModelProviders.of(this).get(ProposicoesViewModel::class.java) }
    private val resultsAdapter by lazy { ProposicaoAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupToolbar()

        pesquisarBtn.setOnClickListener {
            validateAndSearch()
        }
    }

    private fun validateAndSearch() {
        val keywords = keywordsField.text.toString()
        val tipo = tiposSelect.selectedItem as String
        val numero = numeroField.text
        val ano = anoField.text

        if (( (tipo.isNotEmpty() && tipo != "Tipo de Proposição") && numero.isNotEmpty()) ||
                ((tipo.isNotEmpty() && tipo != "Tipo de Proposição") && ano.isNotEmpty()) ||
                (numero.isNotEmpty() && ano.isNotEmpty())) {

            val params = HashMap<String, String>()
            if ((tipo.isNotEmpty() && tipo != "Tipo de Proposição")) params["tipo"] = tipo
            if (numero.isNotEmpty()) params["numero"] = numero.toString()
            if (ano.isNotEmpty()) params["ano"] = ano.toString()
            if (keywords.isNotEmpty()) params["keywords"] = keywords


            emptyText.gone()
            searchProgressBar.visible()

            proposicoesViewModel.fetchPesquisa(params).observe(this, Observer { results ->
                if (results != null) {
                    if (results.isNotEmpty()) {
                        searchProgressBar.gone()
                        emptyText.gone()
                        resultsAdapter.addAll(results)
                        resultadosRecycler.adapter = resultsAdapter
                        resultadosRecycler.layoutManager = LinearLayoutManager(this)
                    } else {
                        searchProgressBar.gone()
                        emptyText.visible()
                        emptyText.text = "Desculpe, sua busca não trouxe resultados"
                    }
                } else {
                    emptyText.visible()
                    searchProgressBar.gone()
                    emptyText.text = "Desculpe, não foi possível buscar as proposições"
                }
            })

        } else {
            Snackbar.make(findViewById(android.R.id.content), "Preencha pelo menos dois dos filtros.", Snackbar.LENGTH_LONG)
                    .show()
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.elevation = 0f
    }
}
