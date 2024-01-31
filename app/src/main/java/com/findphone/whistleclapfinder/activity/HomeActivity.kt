@file:Suppress("DEPRECATION")

package com.findphone.whistleclapfinder.activity

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.SetionClasss.LanguageHelper
import com.findphone.whistleclapfinder.fragments.FindByClapFragment
import com.findphone.whistleclapfinder.fragments.FindByWhistleFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.tabs.TabLayout
import java.util.*


class HomeActivity : AppCompatActivity() {

    var homeFragmentSelectedTab = 0
    private var shouldShowRateUsDialog = true // You can set this flag based on your conditions


    lateinit var viewpager: ViewPager
    lateinit var tabs: TabLayout
    lateinit var toolbarTitle: TextView
    lateinit var settingBtn: ImageView

    var checkMobileData: Boolean? = null

    private val random: Random = Random()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        Log.d("BroadcastReceiver1","HomeActivity OnCreate")


        viewpager = findViewById(R.id.viewpager)
        tabs = findViewById(R.id.tabs)
        toolbarTitle = findViewById(R.id.toolbarTitle)
        settingBtn = findViewById(R.id.settingBtn)




        settingBtn.setOnClickListener {


            startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
            overridePendingTransition(R.anim.enter, R.anim.exit)


        }




        addTabs(viewpager)

        tabs.setupWithViewPager(viewpager)


//        tabs.setSelectedTabIndicatorColor(Color.parseColor("#2D66FA"));
        tabs.setTabTextColors(Color.parseColor("#858B95"), Color.parseColor("#2D66FA"))

// Initial setup
        updateTabBackgroundColors(0) // Set the background for the initially selected tab


        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {


                try {
                    handleTabTextStyle(tab.position, true)
                } catch (e: Exception) {
                }

                homeFragmentSelectedTab = tab.position

                updateMenuOptions()
                updateTabBackgroundColors(tab.position)



                Log.d("changecheck", "onTabSelected")
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
//                updateMenuOptions();

//                handleTabTextStyle(tab.position, false)




                Log.d("changecheck", "onTabUnselected")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }



    private fun updateTabBackgroundColors(selectedTabIndex: Int) {
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                if (i == selectedTabIndex) {
                    tab.view.background = getDrawable(R.drawable.custom_tab_indicator)
                } else {
                    tab.view.background = getDrawable(R.drawable.tab_background)
                }
            }
        }
    }


    private fun updateMenuOptions() {
        try {
//            val save_btn: MenuItem = invoiceActivityMenuRef.findItem(R.id.save_btn)
            if (homeFragmentSelectedTab == 0) {
                Log.d("dde2243ded1sdw", "StorageFragment")


//                save_btn.isVisible = false
            } else if (homeFragmentSelectedTab == 1) {
                Log.d("dde2243ded1sdw", "SettingFragment")
//                save_btn.isVisible = false


            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun handleTabTextStyle(index: Int, selected: Boolean) {
        val tabLayoutContent = (tabs.getChildAt(0) as ViewGroup).getChildAt(index) as LinearLayout
        val tabTextView = tabLayoutContent.getChildAt(1) as TextView
        if (selected) {
            tabTextView.setTypeface(tabTextView.typeface, Typeface.BOLD)
            Log.d("changecheck1","select")
        } else {
            tabTextView.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    private fun addTabs(viewpager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(FindByWhistleFragment(), getString(R.string.find_by_whistle))
        adapter.addFrag(FindByClapFragment(), getString(R.string.find_by_clap))



        viewpager.adapter = adapter
    }


    

    internal class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentStatePagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    override fun onBackPressed() {


        finishAffinity()


        /* below exit dailog and rate us dailog set and show in random number when start work then open  */
//        val randomValue = random.nextInt(3) // Generates a random number between 0 and 2
//
//        if (randomValue == 0) {
//            showExitDialog();
//        } else {
//            showRateUsDialog();
//        }

    }

//    private fun showExitDialog() {
//
//        val dialog = Dialog(this@HomeActivity)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.exit_dailog)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val nodBtn = dialog.findViewById<TextView>(R.id.nodBtn)
//        val yesBtn = dialog.findViewById<TextView>(R.id.yesBtn)
//
//
//        nodBtn.setOnClickListener {
//
//            dialog.dismiss()
//
//        }
//
//        yesBtn.setOnClickListener {
//
////            finish()
//            finishAffinity()
//
//
//        }
//
//
//
//        Log.d("BroadcastReceiver1", "dialog.show();")
//        dialog.show()
//
//
//    }

//    private fun showRateUsDialog() {
//        val dialog = Dialog(this@HomeActivity)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.rate_us_dailog)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val laterBtn = dialog.findViewById<TextView>(R.id.laterBtn)
//        val rateusBtn = dialog.findViewById<TextView>(R.id.rateusBtn)
//        val exitBtn = dialog.findViewById<TextView>(R.id.exitBtn)
//
//
//        exitBtn.setOnClickListener {
//
//            finishAffinity()
//
//        }
//
//        laterBtn.setOnClickListener {
//
//            dialog.dismiss()
//
//        }
//
//        rateusBtn.setOnClickListener {
//
//            openPlayStoreForRating()
//
//        }
//
//
//
//        Log.d("BroadcastReceiver1", "dialog.show();")
//        dialog.show()
//    }

//    private fun openPlayStoreForRating() {
//
//        try {
//            // Replace "com.example.yourapp" with your app's package name
//            val uri = Uri.parse("market://details?id=$packageName")
//            val playStoreIntent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(playStoreIntent)
//        } catch (e: ActivityNotFoundException) {
//            // If Play Store app is not available, open Play Store in browser
//            val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
//            val playStoreIntent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(playStoreIntent)
//        }
//
//
//    }

    override fun onResume() {
        Log.d("BroadcastReceiver1", "onResume HomeActivity")


        super.onResume()
    }

    override fun onStart() {

        // Load the saved language
        LanguageHelper.loadLocale(this)

        super.onStart()



    }



    override fun onRestart() {
        Log.d("BroadcastReceiver1", "onRestart HomeActivity")

        super.onRestart()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {

        Log.d("checksound11111", "onDestroy HomeActivity")


//                        // Set the flag to start as a foreground service
//                        SharedPreferences preferences = getSharedPreferences("whistle_start_service_prefs", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putBoolean("whistl_start_foreground_service", true);
//                        editor.apply();
//        val serviceIntent = Intent(this, MyForegroundService::class.java)
//        startForegroundService(serviceIntent)





        super.onDestroy()
    }




}

