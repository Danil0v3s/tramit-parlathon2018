package br.com.firstsoft.parlathon.view.activities.abs

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import br.com.firstsoft.parlathon.R
import br.com.firstsoft.parlathon.view.adapters.ViewPagerAdapter

abstract class TabActivity : AccountActivity() {

    var viewPagerAdapter: ViewPagerAdapter? = null
    private var tabIcons = intArrayOf(R.drawable.ic_home, R.drawable.ic_discover, R.drawable.ic_bookshelf, R.drawable.ic_profile)
    private var tabIconsActive: MutableList<Drawable> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupGoogle()
        /**
         * TODO Colocar configuracao do facebook no projeto
         */
        //setupFacebook()
    }

    fun setupViews(mainTabLayout: TabLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mainTabLayout.elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, resources.displayMetrics)
            mainTabLayout.translationZ = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
        }
        setupDrawables()
        setupTabCustomView(mainTabLayout)
        setupTabIcons(mainTabLayout)
        setupTabListener(mainTabLayout)
        setupActiveTab(mainTabLayout, 0)
        configureFmListener()
    }

    private fun setupDrawables() {
        for (id in tabIcons) {
            val activeIcon = ContextCompat.getDrawable(this, id)!!
            activeIcon.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, R.color.colorIcon), PorterDuff.Mode.SRC_ATOP)
            tabIconsActive.add(activeIcon)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            try {
                val fragment = supportFragmentManager.findFragmentByTag(supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name)
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun setupTabIcons(mainTabLayout: TabLayout) {
        try {
            for (i in 0 until mainTabLayout.tabCount) {
                val vg = mainTabLayout.getTabAt(i)?.customView as ViewGroup
                val iv = vg.getChildAt(0) as ImageView
                iv.setImageDrawable(ContextCompat.getDrawable(this, tabIcons[i])!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun configureFmListener() {
        supportFragmentManager.addOnBackStackChangedListener { configureBackButton(supportFragmentManager) }
    }

    private fun configureBackButton(fm: FragmentManager) {
        if (fm.backStackEntryCount > 0) {
            setupToolbar(true)
        } else {
            setupToolbar(false)
        }
    }

    private fun setupToolbar(showHomeAsUp: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(showHomeAsUp)
        supportActionBar?.setDisplayShowHomeEnabled(showHomeAsUp)
    }

    private fun clearFragmentStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
        supportFragmentManager.popBackStack()
    }

    private fun setupTabListener(mainTabLayout: TabLayout) {
        mainTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                clearFragmentStack()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun setupActiveTab(mainTabLayout: TabLayout, position: Int) {
        val vg = mainTabLayout.getTabAt(position)?.customView as ViewGroup
        val iv = vg.getChildAt(0) as ImageView
        iv.setImageDrawable(tabIconsActive[position])
    }

    fun setupTabCustomView(mainTabLayout: TabLayout) {
        for (i in 0 until mainTabLayout.tabCount) {
            try {
                val vg = LayoutInflater.from(this).inflate(R.layout.layout_main_tablayout, null) as ViewGroup
                val iv = vg.getChildAt(0) as ImageView
                iv.setImageDrawable(ContextCompat.getDrawable(this, tabIcons[i])!!)
                mainTabLayout.getTabAt(i)!!.customView = vg
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setupViewPager(mainViewPager: ViewPager, mainTabLayout: TabLayout) {
        viewPagerAdapter = (ViewPagerAdapter(supportFragmentManager, this))
        viewPagerAdapter?.addFragment(Fragment(), "")
        viewPagerAdapter?.addFragment(Fragment(), "")
        viewPagerAdapter?.addFragment(Fragment(), "")
        viewPagerAdapter?.addFragment(Fragment(), "")
        mainViewPager.adapter = viewPagerAdapter
        mainViewPager.offscreenPageLimit = 4
        mainViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                when (position) {
                    0 -> {
                        //toolbar_clear?.titleText?.visibility = View.VISIBLE; toolbar_clear?.titleText?.text = "Hoje"
                    }
                    1 -> {
                        //toolbar_clear?.titleText?.visibility = View.VISIBLE; toolbar_clear?.titleText?.text = "Descobrir"
                    }
                    2 -> {
                        //toolbar_clear?.titleText?.visibility = View.VISIBLE; toolbar_clear?.titleText?.text = "Estante"
                    }
                    3 -> {
                        //toolbar_clear?.titleText?.visibility = View.INVISIBLE; toolbar_clear?.titleText?.text = ""
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                setupTabIcons(mainTabLayout)
                setupActiveTab(mainTabLayout, position)
            }

        })
        mainTabLayout.setupWithViewPager(mainViewPager)
    }
}