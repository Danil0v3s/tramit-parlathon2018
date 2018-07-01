package br.com.firstsoft.parlathon.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.data.AppApplication
import br.com.firstsoft.parlathon.view.viewmodel.ProposicoesViewModel

class LauncherActivity : AppCompatActivity() {

    private val proposicoesViewModel by lazy { ViewModelProviders.of(this).get(ProposicoesViewModel::class.java) }
    private var reqCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        proposicoesViewModel.fetchSituacoesProposicao { map -> AppApplication.situacoesProposicao = map; start() }
        proposicoesViewModel.fetchTiposProposicao { map -> AppApplication.tiposProposicao = map; start() }
    }

    private fun start() {
        if (reqCount > 0) --reqCount
        if (reqCount == 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
