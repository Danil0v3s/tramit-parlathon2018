package br.com.firstsoft.parlathon.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.toolbar_dark.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        /**
         * TODO Colocar options menu pra abrir link no chrome
         */

        val link = intent.extras.getString(IntentFieldNames.WEBVIEW_LINK)
        setupWebView(link)
        setupToolbar()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(link: String) {
        val webViewSettings = webView.settings
        webViewSettings.javaScriptEnabled = true
        webViewSettings.javaScriptCanOpenWindowsAutomatically = true
        webViewSettings.useWideViewPort = true
        webViewSettings.loadWithOverviewMode = true
        webViewSettings.setSupportZoom(true)
        webViewSettings.builtInZoomControls = true
        webViewSettings.displayZoomControls = false

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }
        webView.loadUrl(link)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = ""
    }
}
