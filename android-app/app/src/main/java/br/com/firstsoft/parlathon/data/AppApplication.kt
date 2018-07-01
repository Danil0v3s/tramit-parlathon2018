package br.com.firstsoft.parlathon.data

import android.app.Application
import br.com.firstsoft.parlathon.business.vo.SituacaoProposicao
import br.com.firstsoft.parlathon.business.vo.TipoProposicao
import br.com.firstsoft.parlathon.data.di.components.DaggerNetComponent
import br.com.firstsoft.parlathon.data.di.components.DaggerRepositoryComponent
import br.com.firstsoft.parlathon.data.di.components.NetComponent
import br.com.firstsoft.parlathon.data.di.components.RepositoryComponent
import br.com.firstsoft.parlathon.data.di.modules.AppModule
import br.com.firstsoft.parlathon.data.di.modules.NetModule
import br.com.firstsoft.parlathon.data.di.modules.RepositoryModule

class AppApplication : Application() {

    companion object {

        @JvmStatic
        lateinit var instance: AppApplication

        @JvmStatic
        lateinit var NetComponent: NetComponent

        @JvmStatic
        lateinit var RepositoryComponent: RepositoryComponent

        @JvmStatic
        var situacoesProposicao: Map<String, SituacaoProposicao>? = null

        @JvmStatic
        var tiposProposicao: Map<String, TipoProposicao>? = null

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        NetComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule())
                .build()

        RepositoryComponent = DaggerRepositoryComponent.builder()
                .netComponent(NetComponent)
                .repositoryModule(RepositoryModule())
                .build()


    }

}