package com.findphone.whistleclapfinder.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsOnBoardingScreen
import com.google.android.material.tabs.TabLayout


class OnBoardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: OnboardingAdapter
    private lateinit var btnNext: TextView
    private lateinit var btnSkip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val layouts = intArrayOf(
            R.layout.onboarding_screen1,
            R.layout.onboarding_screen2,
            R.layout.onboarding_screen3
        )

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        adapter = OnboardingAdapter(this, layouts)
        viewPager.adapter = adapter


// Kotlin code in your Activity or Fragment

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val dotsContainer = findViewById<LinearLayout>(R.id.dotsContainer)
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)
        btnNext = findViewById<TextView>(R.id.btnNext)



        val selectedDotWidth = resources.getDimensionPixelSize(R.dimen.selected_dot_width) // Define selected dot width in dimens.xml
        val unSelectedDotWidth = resources.getDimensionPixelSize(R.dimen.unselected_dot_width) // Define selected dot width in dimens.xml


        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Update dot indicators based on the current page
                val params1 = dot1.layoutParams as ViewGroup.LayoutParams
                val params2 = dot2.layoutParams as ViewGroup.LayoutParams
                val params3 = dot3.layoutParams as ViewGroup.LayoutParams

                if (position == 0){
                    dot1.setBackgroundResource(R.drawable.tab_dot_selected)
                    params1.width = selectedDotWidth
                    params1.height = unSelectedDotWidth
                }else{
                    dot1.setBackgroundResource(R.drawable.tab_dot_unselected)
                    params1.width = unSelectedDotWidth
                    params1.height = unSelectedDotWidth
                }

                if (position == 1){
                    dot2.setBackgroundResource(R.drawable.tab_dot_selected)
                    params2.width = selectedDotWidth
                    params2.height = unSelectedDotWidth
                }else{
                    dot2.setBackgroundResource(R.drawable.tab_dot_unselected)
                    params2.width = unSelectedDotWidth
                    params2.height = unSelectedDotWidth
                }

                if (position == 2){
                    dot3.setBackgroundResource(R.drawable.tab_dot_selected)
                    params3.width = selectedDotWidth
                    params3.height = unSelectedDotWidth
                }else{
                    dot3.setBackgroundResource(R.drawable.tab_dot_unselected)
                    params3.width = unSelectedDotWidth
                    params3.height = unSelectedDotWidth
                }


            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < 2) {
                viewPager.currentItem += 1
            } else {
                // Handle the case when the user reaches the last onboarding screen
                // You may want to navigate to the main app screen or perform other actions.
                Log.d("checknextbtn","next activity")

                val intent = Intent(this@OnBoardingActivity,PermissionActivity::class.java)
                startActivity(intent)

                MyPrefsOnBoardingScreen.addMyPrefsOnBoardingScreen(this@OnBoardingActivity,true)
            }
        }


        btnSkip.setOnClickListener {

            val intent = Intent(this@OnBoardingActivity,PermissionActivity::class.java)
            startActivity(intent)

            MyPrefsOnBoardingScreen.addMyPrefsOnBoardingScreen(this@OnBoardingActivity,true)

        }


        }


        }



