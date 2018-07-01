package br.com.firstsoft.parlathon.view.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.business.vo.DetalheParlathonProposicao
import br.com.firstsoft.parlathon.business.vo.ParlathonCasaType
import br.com.firstsoft.parlathon.business.vo.ParlathonProposicao
import br.com.firstsoft.parlathon.util.displayAsDate
import br.com.firstsoft.parlathon.util.gone
import br.com.firstsoft.parlathon.util.loadImg
import br.com.firstsoft.parlathon.util.visible
import br.com.firstsoft.parlathon.view.adapters.ParlamentarAdapter
import br.com.firstsoft.parlathon.view.adapters.ProposicaoAdapter
import br.com.firstsoft.parlathon.view.adapters.ProposicaoTimelineAdapter
import br.com.firstsoft.parlathon.view.viewmodel.ProposicoesViewModel
import kotlinx.android.synthetic.main.activity_proposicao_detail.*
import kotlinx.android.synthetic.main.layout_card_proposicao_layout.*
import kotlinx.android.synthetic.main.layout_timeline_basic.*


class ProposicaoDetailActivity : AppCompatActivity() {

    private val proposicoesViewModel by lazy { ViewModelProviders.of(this).get(ProposicoesViewModel::class.java) }
    private val proposicoesTimelineAdapter by lazy { ProposicaoTimelineAdapter(this) }
    private val proposicoesAdapter by lazy { ProposicaoAdapter(this) }
    private val parlamentarAdapter by lazy { ParlamentarAdapter(this) }

    lateinit var proposicao: ParlathonProposicao
    lateinit var detalhesProposicao: DetalheParlathonProposicao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposicao_detail)

        setupToolbar()

        this.proposicao = intent.extras.getSerializable(IntentFieldNames.PROPOSICAO) as ParlathonProposicao

        setupViews(proposicao)
        setupBasicTimeline(proposicao)
    }


    private fun setupViews(proposicao: ParlathonProposicao) {
        val tipo = "Projeto de Lei"

        this.proposicaoName.text = "$tipo ${proposicao.numero}/${proposicao.ano}"
        this.tramitacaoText.text = proposicao.situacao
        this.proposicaoTipo.text = proposicao.siglaTipo
        this.deputadoPicture.loadImg(proposicao.autor.foto)
        this.dataText.text = proposicao.dataApresentacao.displayAsDate()
        this.tipoText.text = tipo

        visualizarIntegraBtn.setOnClickListener {
            val link = "http://www.camara.gov.br/proposicoesWeb/fichadetramitacao?idProposicao=${proposicao.id}"
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(IntentFieldNames.WEBVIEW_LINK, link)
            startActivity(intent)
        }

        visualizarMovimentacoesBtn.setOnClickListener {
            val intent = Intent(this, MovimentacoesActivity::class.java)
            intent.putExtra(IntentFieldNames.PROPOSICAO, proposicao)
            startActivity(intent)
        }

        fetchDetails(proposicao)
//
//        parlamentarAdapter.clear()
//        parlamentarAdapter.addAll(proposicao.autores!!)
//        proposicaoAutoresRecycler.adapter = parlamentarAdapter
//        proposicaoAutoresRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchDetails(proposicao: ParlathonProposicao) {
        if (proposicao.origem == ParlathonCasaType.SENADO_FED) {
            proposicoesViewModel.fetchDetalhesSenado(proposicao.id.toString(), detailCallback())
        } else {
            proposicoesViewModel.fetchDetalhesCamara(proposicao.id.toString(), detailCallback())
        }
    }

    private fun detailCallback(): (DetalheParlathonProposicao?) -> Unit {
        return { detalheParlathonProposicao ->
            detalheParlathonProposicao?.let {
                this.detalhesProposicao = it
                this.assuntoText.text = it.assunto
                this.regimeText.text = it.regime
                this.origemText.text = it.origem

                setupBasicTimeline(it)

                this.transformadoEmProgressBar.gone()
                this.transformadoEmText.visible()

                this.ementaProgressBar.gone()
                this.ementaText.visible()

                val transformado = if (it.transformadoEm.tipo != null) {
                    "${it.transformadoEm.tipo} ${it.transformadoEm.numero}/${it.transformadoEm.ano}"
                } else {
                    "Não disponível"
                }
                this.transformadoEmText.text = transformado
                this.ementaText.text = it.ementa
            }
        }
    }

    private fun setupBasicTimeline(proposicao: Any) {
        if (proposicao is ParlathonProposicao) {
            this.iniciadoraNameText.text = proposicao.casa.nome
            this.iniciadoraDateText.text = proposicao.dataApresentacao.displayAsDate()

            if (proposicao.origem == ParlathonCasaType.CAMARA_DPT) {
                this.revisoraNameText.text = "Senado Federal"
            } else {
                this.revisoraNameText.text = "Câmara dos Deputados"
            }
        }

        if (proposicao is DetalheParlathonProposicao) {
            proposicao.linhaDoTempo.casaIniciadora?.let {
                if (proposicao.casa.id == ParlathonCasaType.CAMARA) {
                    this.iniciadoraIndicator.background = getDrawable(R.drawable.dot_camara_deputados)
                    this.iniciadoraLine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue))
                } else if (proposicao.casa.id == ParlathonCasaType.SENADO) {
                    this.iniciadoraIndicator.background = getDrawable(R.drawable.dot_senado)
                    this.iniciadoraLine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOrange))
                }
            }

            proposicao.linhaDoTempo.casaRevisora?.let {
                this.revisoraDateText.visible()
                this.revisoraDateText.text = it
                if (proposicao.casa.id == ParlathonCasaType.CAMARA) {
                    this.revisoraIndicator.background = getDrawable(R.drawable.dot_camara_deputados)
                    this.revisoraLine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue))
                } else if (proposicao.casa.id == ParlathonCasaType.SENADO) {
                    this.revisoraIndicator.background = getDrawable(R.drawable.dot_senado)
                    this.revisoraLine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOrange))
                }
            }

            proposicao.linhaDoTempo.sancaoPresidencial?.let {
                this.sancaoDateText.visible()
                this.sancaoDateText.text = it
                this.sancaoIndicator.background = getDrawable(R.drawable.dot_congresso)
                this.sancaoLine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreenDark))
            }

            proposicao.linhaDoTempo.promulgacaoEPublicacao?.let {
                this.promulgacaoDateText.visible()
                this.promulgacaoDateText.text = it
                this.promulgacaoIndicator.background = getDrawable(R.drawable.dot_congresso)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.elevation = 0f
    }
}
