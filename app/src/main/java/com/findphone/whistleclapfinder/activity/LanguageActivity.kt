package com.findphone.whistleclapfinder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.languageClasess.LanguageAdapter
import com.findphone.whistleclapfinder.languageClasess.LanguageModel

class LanguageActivity : AppCompatActivity() {

    lateinit var languageRecyclerView: RecyclerView
    lateinit var checkBtn: ImageView
    lateinit var backPressBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        languageRecyclerView = findViewById(R.id.languageRecyclerView)
        checkBtn = findViewById(R.id.checkBtn)
        backPressBtn = findViewById(R.id.backPressBtn)



        backPressBtn.setOnClickListener {

//            val intent = Intent(this@LanguageActivity,SettingActivity::class.java)
//            startActivity(intent)

            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }





        val languages = listOf(
            LanguageModel(getString(R.string.english), "en", R.drawable.ic_english_flag),
            LanguageModel(getString(R.string.arabic), "ar", R.drawable.ic_arabic_flag),
            LanguageModel(getString(R.string.spanish), "es", R.drawable.ic_spanish_flag),
            LanguageModel(getString(R.string.hindi), "hi", R.drawable.ic_hindi_flag)
        )

        val adapter = LanguageAdapter(checkBtn,this, languages) { language ->
            // Handle item click here
            Toast.makeText(this, "Selected language: ${language.name}", Toast.LENGTH_SHORT).show()

            Log.d("language",language.name)


        }

        languageRecyclerView.layoutManager = LinearLayoutManager(this)
        languageRecyclerView.adapter = adapter


    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}