package br.com.firstsoft.parlathon.view.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.widget.Toast
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.business.model.ListType
import br.com.firstsoft.parlathon.business.model.ViewType
import br.com.firstsoft.parlathon.util.gone
import br.com.firstsoft.parlathon.util.visible
import br.com.firstsoft.parlathon.view.adapters.ParlamentarAdapter
import br.com.firstsoft.parlathon.view.adapters.PartidoAdapter
import br.com.firstsoft.parlathon.view.adapters.ProposicaoAdapter
import br.com.firstsoft.parlathon.view.viewholder.ParlamentarViewHolder
import br.com.firstsoft.parlathon.view.viewholder.ProposicaoViewHolder
import br.com.firstsoft.parlathon.view.viewmodel.DeputadosViewModel
import br.com.firstsoft.parlathon.view.viewmodel.PartidosViewModel
import br.com.firstsoft.parlathon.view.viewmodel.ProposicoesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.util.Pair as AndroidPair

class MainActivity : AppCompatActivity() {

    private val proposicoesViewModel by lazy { ViewModelProviders.of(this).get(ProposicoesViewModel::class.java) }
    private val deputadosViewModel by lazy { ViewModelProviders.of(this).get(DeputadosViewModel::class.java) }
    private val partidosViewModel by lazy { ViewModelProviders.of(this).get(PartidosViewModel::class.java) }
    private val projetosCamaraAdapter by lazy { ProposicaoAdapter(this) }
    private val projetosSenadoAdapter by lazy { ProposicaoAdapter(this) }
    private val parlamentarAdapter by lazy { ParlamentarAdapter(this) }
    private val partidoAdapter by lazy { PartidoAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView(savedInstanceState)
        setupViews()
        observeDeputados()
        observeProposicoes()
        observePartidos()
    }

    private fun observeProposicoes() {
        proposicoesViewModel.fetchProjetosCamara().observe(this, Observer { projetos ->
            if (projetos != null) {
                camaraProgressBar.gone()
                proposicoesRecycler.visible()
                projetosCamaraAdapter.clear()
                projetosCamaraAdapter.addAll(projetos)
            }
        })

        proposicoesViewModel.fetchProjetosSenado().observe(this, Observer { projetos ->
            if (projetos != null) {
                senadoProgressBar.gone()
                proposicoesSenadoRecycler.visible()
                projetosSenadoAdapter.clear()
                projetosSenadoAdapter.addAll(projetos)
            }
        })

    }

    private fun observeDeputados() {
        deputadosViewModel.fetchDeputados().observe(this, Observer { deputados ->
            if (deputados != null) {
                parlamentarAdapter.clear()
                parlamentarAdapter.addAll(deputados)
            } else {
                Toast.makeText(this, "Lista nula", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observePartidos() {
        partidosViewModel.fetchPartidos().observe(this, Observer { partidos ->
            if (partidos != null) {
                partidoAdapter.clear()
                partidoAdapter.addAll(partidos)
            } else {
                Toast.makeText(this, "Lista nula", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            rootLayout.visibility = View.INVISIBLE
            val viewTreeObserver = rootLayout.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        rootLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }
    }

    private fun setupViews() {
        setupProposicoesRecycler()
        setupParlamentaresRecycler()
        setupPartidosRecycler()

        parlamentaresVerMaisBtn.setOnClickListener {
            deputadosViewModel.deputadosThumb.value?.let {
                if (it.isNotEmpty()) {
                    it as ArrayList<ViewType>
                    startActivityWithTransition(parlamentaresRecycler.layoutManager as LinearLayoutManager, it, ListType.DEPUTADO)
                }
            }
        }

        proposicoesCamaraVerMaisBtn.setOnClickListener {
            proposicoesViewModel.projetosCamara.value?.let {
                if (it.isNotEmpty()) {
                    it as ArrayList<ViewType>
                    startActivityWithTransition(proposicoesRecycler.layoutManager as LinearLayoutManager, it, ListType.PROPOSICAO)
                }
            }
        }

        proposicoesSenadoVerMaisBtn.setOnClickListener {
            proposicoesViewModel.projetosSenado.value?.let {
                if (it.isNotEmpty()) {
                    it as ArrayList<ViewType>
                    startActivityWithTransition(proposicoesRecycler.layoutManager as LinearLayoutManager, it, ListType.PROPOSICAO)
                }
            }
        }

        informacoesBtn.setOnClickListener {
            startActivity(Intent(this, TramitInfoActivity::class.java))
        }

        procurarBtn.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun setupProposicoesRecycler() {
        proposicoesRecycler.adapter = projetosCamaraAdapter
        proposicoesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        proposicoesRecycler.setHasFixedSize(true)

        proposicoesSenadoRecycler.adapter = projetosSenadoAdapter
        proposicoesSenadoRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        proposicoesSenadoRecycler.setHasFixedSize(true)
    }

    private fun setupParlamentaresRecycler() {
        parlamentaresRecycler.adapter = parlamentarAdapter
        parlamentaresRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        parlamentaresRecycler.setHasFixedSize(true)
    }

    private fun setupPartidosRecycler() {
        partidosRecycler.adapter = partidoAdapter
        partidosRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        partidosRecycler.setHasFixedSize(true)
    }

    private fun startActivityWithTransition(layoutManager: LinearLayoutManager, items: ArrayList<ViewType>, listType: ListType) {
        /**
         * Testar entre findFirstCompletelyVisibleItemPosition() e findFirstVisibleItemPosition()
         */

        val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

        val pairs: MutableList<AndroidPair<View, String>> = mutableListOf()
        for (i in firstVisibleItemPosition until lastVisibleItemPosition) {

            if (listType == ListType.DEPUTADO) {
                val holderForAdapterPosition = parlamentaresRecycler.findViewHolderForAdapterPosition(i) as ParlamentarViewHolder
                val imageView = holderForAdapterPosition.deputadoPicture
                pairs.add(AndroidPair(imageView, "view_$i"))
            } else if (listType == ListType.PROPOSICAO) {
                val holderForAdapterPosition = parlamentaresRecycler.findViewHolderForAdapterPosition(i) as ProposicaoViewHolder
                val imageView = holderForAdapterPosition.deputadoPicture
                pairs.add(AndroidPair(imageView, "view_$i"))
            }

        }

        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs.toTypedArray()).toBundle()

        val intent = Intent(this, ViewMoreActivity::class.java)
        intent.putExtra(IntentFieldNames.ITEMS, items)
        intent.putExtra(IntentFieldNames.LIST_TYPE, listType)
        intent.putExtra(IntentFieldNames.FIRST_VISIBLE_ITEM, firstVisibleItemPosition)
        ActivityCompat.startActivity(this, intent, bundle)

    }

    private fun circularRevealActivity() {

        val cx = rootLayout.width / 2
        val cy = rootLayout.height / 2

        val finalRadius = Math.max(rootLayout.width, rootLayout.height)

        val circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0f, finalRadius.toFloat())
        circularReveal.duration = 1000
        rootLayout.visibility = View.VISIBLE
        circularReveal.start()

    }
}
