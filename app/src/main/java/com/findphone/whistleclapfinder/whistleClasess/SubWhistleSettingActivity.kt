package com.findphone.whistleclapfinder.whistleClasess

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.clapsClasess.clapsdatabase.AppDatabase
import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.AudioModelWhisleDatabase
import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.WhistleAudioModelDao


class SubWhistleSettingActivity : AppCompatActivity(),
    WhistleItemClickListener {

    lateinit var seekbarSound: SeekBar
    lateinit var tenTextview: TextView
    lateinit var fifteenTextview: TextView
    lateinit var tharteenTextview: TextView
    lateinit var oneMinutTextview: TextView
    lateinit var progressTextview: TextView
    lateinit var toolbarTitle: TextView
    lateinit var oneMinutLayout: LinearLayout
    lateinit var tharteenSecondLayout: LinearLayout
    lateinit var fifteenSecondLayout: LinearLayout
    lateinit var tenSecondLayout: LinearLayout
    lateinit var vibration_check: SwitchCompat
    lateinit var flash_check: SwitchCompat
    lateinit var sound_check: SwitchCompat
    lateinit var backPressBtn: ImageView
    lateinit var applyBtn: TextView
    lateinit var imageChangeRecylerview: RecyclerView
    private var settingImageChangeAdapter: WhistleSubSettingImageChangeAdapter? = null
    private var changeImageItems: ArrayList<ChageImageModel>? = null
    private var changeImageItems1: ArrayList<AudioModelWhisleDatabase>? = null
    private var imageResourceIdStoreCheck: String?=null
    private var idUpdated: Int?=null

    private var database: AppDatabase? = null
    private var whistleAudioModelDao: WhistleAudioModelDao? = null
    var updateId:Int = 0
    var updateProgress:Int = 0
    var updateDuration:Int = 0


    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_whistle_setting)

        database = AppDatabase.getDatabase(this) as AppDatabase
        whistleAudioModelDao = database!!.whistleAudioModelDao()

        backPressBtn = findViewById(R.id.backPressBtn)
        imageChangeRecylerview = findViewById(R.id.imageChangeRecylerview)
        applyBtn = findViewById(R.id.applyBtn)
        vibration_check = findViewById(R.id.vibration_check)
        flash_check = findViewById(R.id.flash_check)
        sound_check = findViewById(R.id.sound_check)
        seekbarSound = findViewById(R.id.seekbarSound)
        toolbarTitle = findViewById(R.id.toolbarTitle)
        oneMinutLayout = findViewById(R.id.oneMinutLayout)
        tharteenSecondLayout = findViewById(R.id.tharteenSecondLayout)
        fifteenSecondLayout = findViewById(R.id.fifteenSecondLayout)
        tenSecondLayout = findViewById(R.id.tenSecondLayout)
        progressTextview = findViewById(R.id.progressTextview)
        tenTextview = findViewById(R.id.tenTextview)
        fifteenTextview = findViewById(R.id.fifteenTextview)
        tharteenTextview = findViewById(R.id.tharteenTextview)
        oneMinutTextview = findViewById(R.id.oneMinutTextview)


        sound_check.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked){
                seekbarSound.isEnabled = true
                Log.d("receivedAud1111ioModel1112","   1   "+  isChecked.toString())
                seekbarSound.thumb = ContextCompat.getDrawable(this,R.drawable.thumb_icon)
                val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.buttons_switch_color)) // Replace with your desired color resource
                seekbarSound.setProgressTintList(colorStateList)

            }else{
                seekbarSound.isEnabled = false
                Log.d("receivedAud1111ioModel1112","   1   "+  isChecked.toString())
                seekbarSound.thumb = ContextCompat.getDrawable(this,R.drawable.thumb_unselected_icon)
                val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.unselected_color)) // Replace with your desired color resource
                seekbarSound.setProgressTintList(colorStateList)
            }

        }



            // Now you have the AudioModel object
        val myList = intent.getSerializableExtra("audioModelByteArray") as ArrayList<AudioModelWhisleDatabase>?
        val WhistleAudioAdapter_Id = intent.getIntExtra("WhistleAudioAdapter_Id",0)
        val position = intent.getIntExtra("position",0)
        if (myList != null) {

            Log.d("checkid", "SubWhistleSettingActivity id   "+WhistleAudioAdapter_Id)

            Log.d("receivedAud1111ioModel",myList.get(position).title)
            flash_check.isChecked = myList[position].isFlashLight
            vibration_check.isChecked = myList[position].isVibration
            sound_check.isChecked = myList[position].isSound
            seekbarSound.progress = myList[position].soundSeekbar
            toolbarTitle.text =  myList[position].title
            progressTextview.text = myList[position].soundSeekbar.toString()
            updateId = myList[position].id
            updateDuration = myList[position].timeDurationService
            updateProgress = myList[position].soundSeekbar


            if (sound_check.isChecked){
                seekbarSound.isEnabled = true
                Log.d("receivedAud1111ioModel1112","   2   "+ sound_check.isChecked.toString())
                seekbarSound.thumb = ContextCompat.getDrawable(this,R.drawable.thumb_icon)
                val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.buttons_switch_color)) // Replace with your desired color resource
                seekbarSound.setProgressTintList(colorStateList)
            }else{
                seekbarSound.isEnabled = false
                Log.d("receivedAud1111ioModel1112", "   2   "+ sound_check.isChecked.toString())

                seekbarSound.thumb = ContextCompat.getDrawable(this,R.drawable.thumb_unselected_icon)
                val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.unselected_color)) // Replace with your desired color resource
                seekbarSound.setProgressTintList(colorStateList)
            }

            if (myList[position].timeDurationService==10000){

                tenSecondLayoutBackground()

            }else if (myList[position].timeDurationService==15000){

                tenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)

                fifteenSecondLayoutBackground()

            }else if (myList[position].timeDurationService==30000){

                tharteenSecondLayoutBackground()

            }else if (myList[position].timeDurationService==60000){


                oneMinutLayoutBackground()

            }




        } else {
            // Handle the case where the byte array was not passed correctly
            Log.d("receivedAudioModel","null")
        }



        imageChangeRecylerview.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))


//        changeImageItems = ArrayList()
//        changeImageItems!!.add(ChageImageModel("dog",imageConverter(this, R.drawable.dog_icon)))
//        changeImageItems!!.add(ChageImageModel("cat",imageConverter(this, R.drawable.cat_icon)))
//        changeImageItems!!.add(ChageImageModel("azaan",imageConverter(this, R.drawable.ic_launcher_background)))



        settingImageChangeAdapter = WhistleSubSettingImageChangeAdapter(this,myList!!,WhistleAudioAdapter_Id,this@SubWhistleSettingActivity)
        imageChangeRecylerview.setAdapter(settingImageChangeAdapter)


        seekbarSound.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                updateProgress = progress
                progressTextview.text = progress.toString()


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })


        backPressBtn.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }

        applyBtn.setOnClickListener {

            Log.d("checkid", " 3  "+idUpdated.toString())
            Log.d("checkid", " imageResourceIdStore   applyBtn  "+imageResourceIdStoreCheck)




            var batterySavingUpdate = AudioModelWhisleDatabase()
//            batterySavingUpdate.id = updateId  // this id get in my list
            batterySavingUpdate.id = idUpdated!!   // this id get in WhistleSubSettingImageChangeAdapter
            batterySavingUpdate.title = toolbarTitle.text.toString()
            batterySavingUpdate.imageResourceId = imageResourceIdStoreCheck // this image get in WhistleSubSettingImageChangeAdapter
            batterySavingUpdate.isFlashLight = flash_check.isChecked
            batterySavingUpdate.isVibration = vibration_check.isChecked
            batterySavingUpdate.isSound = sound_check.isChecked
            batterySavingUpdate.soundSeekbar = updateProgress
            batterySavingUpdate.timeDurationService = updateDuration


           UpdateData(batterySavingUpdate)

        }

        tenSecondLayout.setOnClickListener {
            secondClickItems(10000)  // 10000 means 10 seconds

            tenSecondLayoutBackground()
        }

        fifteenSecondLayout.setOnClickListener {
            secondClickItems(15000)  // 15000 means 15 seconds

            fifteenSecondLayoutBackground()
        }

        tharteenSecondLayout.setOnClickListener {
            secondClickItems(30000) // 30000 means 30 seconds

            tharteenSecondLayoutBackground()
        }

        oneMinutLayout.setOnClickListener {
            secondClickItems(60000)  // 60000 means 1 minute

            oneMinutLayoutBackground()
        }

    }

    private fun UpdateData(batterySavingUpdate: AudioModelWhisleDatabase) {

        val thread = Thread {
            try {
                // Update database
                whistleAudioModelDao?.updateBatterySaveing(batterySavingUpdate)

                // Update UI on the main thread
                runOnUiThread(Runnable {
                    Log.d("dde2243111ded", " ID  " + "Update Data")

                    val audioResourceId: Int = resources.getIdentifier(batterySavingUpdate.title, "raw", packageName) // set audio audioResourceId means complete path audio


                    val sharedPreferences: SharedPreferences = getSharedPreferences("MyPreferencesSelectItem", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("image_key", batterySavingUpdate.imageResourceId)
                    editor.putString("nameTitle", batterySavingUpdate.title)
                    editor.putBoolean("flashLight", batterySavingUpdate.isFlashLight)
                    editor.putBoolean("isVibration", batterySavingUpdate.isVibration)
                    editor.putInt("soundSeekbar", batterySavingUpdate.soundSeekbar)
                    editor.putBoolean("isSound", batterySavingUpdate.isSound)
                    editor.putInt("timeDurationService", batterySavingUpdate.timeDurationService)
                    editor.putBoolean("isRingTuneSelected", true)
                    editor.putInt("audioResourceId", audioResourceId)
                    editor.apply()

                    // LocalBroadcastManager use for refresh icon in on off button
                    val intent1 = Intent("ChangeIconSelectedBroadcastManagerWhistle")
                    intent1.putExtra("key", "My Data")
                    // put your all data using put extra
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1)





                    onBackPressedDispatcher.onBackPressed()
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                     Toast.makeText(this, applyBtn.text.toString(), Toast.LENGTH_SHORT).show();
                })
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
        thread.start()


    }


    private fun tenSecondLayoutBackground() {
        tenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_active_layout_bg)
        fifteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        tharteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        oneMinutLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)

        tenTextview.setTextColor(Color.parseColor("#FFFFFFFF"))
        fifteenTextview.setTextColor(Color.parseColor("#2D81FA"))
        tharteenTextview.setTextColor(Color.parseColor("#2D81FA"))
        oneMinutTextview.setTextColor(Color.parseColor("#2D81FA"))


    }

    private fun fifteenSecondLayoutBackground() {
        tenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        fifteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_active_layout_bg)
        tharteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        oneMinutLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)


        tenTextview.setTextColor(Color.parseColor("#2D81FA"))
        fifteenTextview.setTextColor(Color.parseColor("#FFFFFFFF"))
        tharteenTextview.setTextColor(Color.parseColor("#2D81FA"))
        oneMinutTextview.setTextColor(Color.parseColor("#2D81FA"))
    }

    private fun tharteenSecondLayoutBackground() {
        tenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        fifteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        tharteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_active_layout_bg)
        oneMinutLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)

        tenTextview.setTextColor(Color.parseColor("#2D81FA"))
        fifteenTextview.setTextColor(Color.parseColor("#2D81FA"))
        tharteenTextview.setTextColor(Color.parseColor("#FFFFFFFF"))
        oneMinutTextview.setTextColor(Color.parseColor("#2D81FA"))
    }


    private fun oneMinutLayoutBackground() {
        tenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        fifteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        tharteenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)
        oneMinutLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_active_layout_bg)

        tenTextview.setTextColor(Color.parseColor("#2D81FA"))
        fifteenTextview.setTextColor(Color.parseColor("#2D81FA"))
        tharteenTextview.setTextColor(Color.parseColor("#2D81FA"))
        oneMinutTextview.setTextColor(Color.parseColor("#FFFFFFFF"))
    }


    private fun secondClickItems(seconds: Int) {
        updateDuration = seconds

    }

    // SubSettingImageChangeAdapter image change adapter on click
    override fun onItemClick(id: Int,imageResourceIdStore: String,audioModelWhisleDatabase: AudioModelWhisleDatabase) {
        imageResourceIdStoreCheck = imageResourceIdStore
        idUpdated = id

        Log.d("checkid", " 2  "+id.toString())




        flash_check.isChecked = audioModelWhisleDatabase.isFlashLight
        vibration_check.isChecked = audioModelWhisleDatabase.isVibration
        sound_check.isChecked = audioModelWhisleDatabase.isSound
        seekbarSound.progress = audioModelWhisleDatabase.soundSeekbar
        toolbarTitle.text =  audioModelWhisleDatabase.title
        progressTextview.text = audioModelWhisleDatabase.soundSeekbar.toString()
        updateId = audioModelWhisleDatabase.id
        updateDuration = audioModelWhisleDatabase.timeDurationService
        updateProgress = audioModelWhisleDatabase.soundSeekbar


        if (sound_check.isChecked){
            seekbarSound.isEnabled = true
            Log.d("receivedAud1111ioModel1112","   2   "+ sound_check.isChecked.toString())

            seekbarSound.thumb = ContextCompat.getDrawable(this,R.drawable.thumb_icon)
            val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.buttons_switch_color)) // Replace with your desired color resource
            seekbarSound.setProgressTintList(colorStateList)
        }else{
            seekbarSound.isEnabled = false
            Log.d("receivedAud1111ioModel1112", "   2   "+ sound_check.isChecked.toString())
            seekbarSound.thumb = ContextCompat.getDrawable(this,R.drawable.thumb_unselected_icon)
            val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.unselected_color)) // Replace with your desired color resource
            seekbarSound.setProgressTintList(colorStateList)
        }

        if (audioModelWhisleDatabase.timeDurationService==10000){

            tenSecondLayoutBackground()

        }else if (audioModelWhisleDatabase.timeDurationService==15000){

            tenSecondLayout.background = ContextCompat.getDrawable(this,R.drawable.duration_item_border_color_bg)

            fifteenSecondLayoutBackground()

        }else if (audioModelWhisleDatabase.timeDurationService==30000){

            tharteenSecondLayoutBackground()

        }else if (audioModelWhisleDatabase.timeDurationService==60000){


            oneMinutLayoutBackground()

        }


    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}