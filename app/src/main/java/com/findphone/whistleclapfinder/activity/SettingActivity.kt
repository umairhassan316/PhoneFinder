package com.findphone.whistleclapfinder.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.SetionClasss.LanguageHelper
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsExtensionMainSetting.addMyPrefsExtensionMainSetting
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsExtensionMainSetting.getMyPrefsExtensionMainSetting
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForMainSettingFlashLightModeSelect
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForMainSettingFlashLightModeSelect.addMyPrefsUseForMainSettingFlashLightModeSelect
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsUseForOnlyHeartbeatVibration
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsVibrationModeMainSetting.addMyPrefsMyPrefsVibrationModeMainSetting
import com.findphone.whistleclapfinder.SetionClasss.MyPrefsVibrationModeMainSetting.getMyPrefsMyPrefsVibrationModeMainSetting
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightDefualt
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightDiscoMode
import com.findphone.whistleclapfinder.settingClassess.SettingFlashLightSOSMode


class SettingActivity : AppCompatActivity() {

    lateinit var defaultVibration:RadioButton
    lateinit var strongVibration:RadioButton
    lateinit var heartbeat:RadioButton
    lateinit var tiktok:RadioButton
    lateinit var flashdefult:RadioButton
    lateinit var discoMode:RadioButton
    lateinit var sosMode:RadioButton
    lateinit var flashModeRadioGroupBtn:RadioGroup
    lateinit var vibrationModeRadioGroupBtn:RadioGroup
    lateinit var backPressBtn:ImageView
    lateinit var extention_check:SwitchCompat
    lateinit var seekBarFrequency:SeekBar
    lateinit var languageBtn:LinearLayout
    lateinit var languageName:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)




        languageName = findViewById(R.id.languageName)
        languageBtn = findViewById(R.id.languageBtn)
        seekBarFrequency = findViewById(R.id.seekbarSensitivity)
        defaultVibration = findViewById(R.id.defaultVibration)
        strongVibration = findViewById(R.id.strongVibration)
        heartbeat = findViewById(R.id.heartbeat)
        tiktok = findViewById(R.id.tiktok)
        flashdefult = findViewById(R.id.flashdefult)
        discoMode = findViewById(R.id.discoMode)
        sosMode = findViewById(R.id.sosMode)
        backPressBtn = findViewById(R.id.backPressBtn)
        extention_check = findViewById(R.id.extention_check)
        flashModeRadioGroupBtn = findViewById(R.id.flashModeRadioGroupBtn)
        vibrationModeRadioGroupBtn = findViewById(R.id.vibrationModeRadioGroupBtn)

        backPressBtn.setOnClickListener {

//            val slideactivity = Intent(this@SettingActivity, HomeActivity::class.java)
//            val bndlanimation = ActivityOptions.makeCustomAnimation(applicationContext, R.anim.exit, R.anim.enter).toBundle()
//            startActivity(slideactivity, bndlanimation)
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


        }

        languageBtn.setOnClickListener {

//            val intent = Intent(this@SettingActivity,LanguageActivity::class.java)
//            startActivity(intent)

            startActivity(Intent(this@SettingActivity, LanguageActivity::class.java))
            overridePendingTransition(R.anim.enter, R.anim.exit)

        }


        val isCheckedSharedPrefrence = getMyPrefsExtensionMainSetting(this)

            extention_check.isChecked = isCheckedSharedPrefrence


        extention_check.setOnCheckedChangeListener { buttonView, isChecked ->

            addMyPrefsExtensionMainSetting(this,isChecked)

        }


        // is check main setting add functionality flash light means user who apply flashlight mode like default or discoMode or sosMode
        val flashLightMode = MyPrefsUseForMainSettingFlashLightModeSelect.getMyPrefsUseForMainSettingFlashLightModeSelect(this)

        if (flashLightMode == "default") {

            flashdefult.isChecked = true

        } else if (flashLightMode == "discoMode") {
            discoMode.isChecked = true

        } else if (flashLightMode == "sosMode") {
            sosMode.isChecked = true
        }




        flashModeRadioGroupBtn.setOnCheckedChangeListener { group, checkedId ->

            val defultButton = group.findViewById(checkedId) as RadioButton

            if (defultButton.text.equals("default")){

                val defaultIsChecked = true
                val discoModeIsChecked = false
                val sosModeIsChecked = false


                onOffFlashLight(defaultIsChecked,discoModeIsChecked,sosModeIsChecked)

                // haler use for stop flash light after run 5 second
                handlerUseForStopFlashLight()

            }else if (defultButton.text.equals("discoMode")){

                val defaultIsChecked = false
                val discoModeIsChecked = true
                val sosModeIsChecked = false

                onOffFlashLight(defaultIsChecked,discoModeIsChecked,sosModeIsChecked)

                // haler use for stop flash light after run 5 second
                handlerUseForStopFlashLight()

            }else if (defultButton.text.equals("sosMode")){


                val defaultIsChecked = false
                val discoModeIsChecked = false
                val sosModeIsChecked = true

                onOffFlashLight(defaultIsChecked,discoModeIsChecked,sosModeIsChecked)

                // haler use for stop flash light after run 5 second
                handlerUseForStopFlashLight()

            }


            // ADD_FLASH_LIGHT_MODE_NAME  this class MyPrefsUseForMainSettingFlashLightModeSelect
            addMyPrefsUseForMainSettingFlashLightModeSelect(this,defultButton.text.toString())

            Log.d("check1234",defultButton.text.toString())




        }




        // is check main setting add functionality flash light means user who apply flashlight mode like default or discoMode or sosMode
        val vibrationModeName = getMyPrefsMyPrefsVibrationModeMainSetting(this)

        if (vibrationModeName == "defaultVibration") {

            defaultVibration.isChecked = true

        } else if (vibrationModeName == "strong vibration") {
            strongVibration.isChecked = true

        } else if (vibrationModeName == "heartbeat") {
            heartbeat.isChecked = true
        }
        else if (vibrationModeName == "tiktok") {
            tiktok.isChecked = true
        }



        vibrationModeRadioGroupBtn.setOnCheckedChangeListener { group, checkedId ->

            val defultButton = group.findViewById(checkedId) as RadioButton

            if (defultButton.text.equals("defaultVibration")){

                val defaultIsChecked = true
                val strongVibrationIsChecked = false
                val heartbeatVibrationIsChecked = false
                val tikTokVibrationIsChecked = false


                onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked)

                // haler use for stop vibration after run 5 second
                handlerUseForStopVibration()

            }else if (defultButton.text.equals("strong vibration")){

                val defaultIsChecked = false
                val strongVibrationIsChecked = true
                val heartbeatVibrationIsChecked = false
                val tikTokVibrationIsChecked = false


                onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked)

                // haler use for stop vibration after run 5 second
                handlerUseForStopVibration()

            }else if (defultButton.text.equals("heartbeat")){

                val defaultIsChecked = false
                val strongVibrationIsChecked = false
                val heartbeatVibrationIsChecked = true
                val tikTokVibrationIsChecked = false


                onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked)

                // haler use for stop vibration after run 5 second
                handlerUseForStopVibration()

            }else if (defultButton.text.equals("tiktok")){

                val defaultIsChecked = false
                val strongVibrationIsChecked = false
                val heartbeatVibrationIsChecked = false
                val tikTokVibrationIsChecked = true


                onOffVibration(defaultIsChecked,strongVibrationIsChecked,heartbeatVibrationIsChecked,tikTokVibrationIsChecked)

                // haler use for stop vibration after run 5 second
                handlerUseForStopVibration()
            }

            // ADD_VIBRATION_MODE_NAME  this class MyPrefsVibrationModeMainSetting
            addMyPrefsMyPrefsVibrationModeMainSetting(this,defultButton.text.toString())

        }

    }


    private fun handlerUseForStopVibration() {

        Handler(Looper.getMainLooper()).postDelayed({

            onOffVibration(defaultIsChecked = false, strongVibrationIsChecked = false, heartbeatVibrationIsChecked = false, tikTokVibrationIsChecked = false)

        }, 3000)



    }

    private fun handlerUseForStopFlashLight() {

        Handler(Looper.getMainLooper()).postDelayed({

            onOffFlashLight(false,false,false)

        }, 3000)


    }

    private fun onOffVibration(defaultIsChecked: Boolean, strongVibrationIsChecked: Boolean, heartbeatVibrationIsChecked: Boolean, tikTokVibrationIsChecked: Boolean) {

        // clear MHeartbeat Vibration
        MyPrefsUseForOnlyHeartbeatVibration.clearMyPrefsUseForOnlyHeartbeatVibration(this@SettingActivity)


        // Turn on the flashlight

        SettingFlashLightDefualt.isVibrationSetDefault(this,defaultIsChecked)
        SettingFlashLightDefualt.isVibrationSetStrong(this,strongVibrationIsChecked)
        SettingFlashLightDefualt.isVibrationSetHeartbeat(this,heartbeatVibrationIsChecked)
        SettingFlashLightDefualt.isVibrationSetTickTok(this,tikTokVibrationIsChecked)
    }




    private fun onOffFlashLight(defaultIsChecked: Boolean, discoModeIsChecked: Boolean, sosModeIsChecked: Boolean) {

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var cameraId: String? = null

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            throw RuntimeException(e)
        }

        // defualt flash light OFF
        SettingFlashLightDefualt.startFlashlightBlinking(cameraManager, cameraId, defaultIsChecked, this)

        // disco mode flash light OFF
        SettingFlashLightDiscoMode.startFlashlightBlinking(cameraManager, cameraId, discoModeIsChecked, this)


        // sos Mode flash light ON
        SettingFlashLightSOSMode.startFlashlightBlinking(cameraManager, cameraId, sosModeIsChecked, this)


    }

    override fun onStart() {
        super.onStart()
        // Load the saved language
       LanguageHelper.loadLocale(this)
        val languageNameGet = LanguageHelper.getMyPrefsLanguageName(this)


        Log.d("checklanguagename", languageNameGet.toString())

        languageName.text = languageNameGet

    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}