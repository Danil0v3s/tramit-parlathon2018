package br.com.firstsoft.parlathon.view.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.business.model.IntentFieldNames
import br.com.firstsoft.parlathon.view.adapters.ViewPagerAdapter
import br.com.firstsoft.parlathon.view.fragments.TramitInfoFragment
import kotlinx.android.synthetic.main.activity_tramit_info.*
import kotlinx.android.synthetic.main.toolbar_clear.*

class TramitInfoActivity : AppCompatActivity() {

    val resourceIDs = intArrayOf(R.drawable.projeto_um, R.drawable.projeto_dois, R.drawable.projeto_tres, R.drawable.projeto_quartro, R.drawable.projeto_cinco, R.drawable.projeto_seis)
    val viewPagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tramit_info)
        initView(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        for (resource in resourceIDs) {
            val fragment = TramitInfoFragment()
            val args = Bundle()
            args.putInt(IntentFieldNames.RES_ID, resource)
            fragment.arguments = args
            viewPagerAdapter.addFragment(fragment, "")
        }
        tramitInfoViewPager.adapter = viewPagerAdapter
        tramitInfoViewPager.currentItem = 0
        tramitInfoViewPager.offscreenPageLimit = 0


        tramitInfoTabLayout.setupWithViewPager(tramitInfoViewPager)

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
