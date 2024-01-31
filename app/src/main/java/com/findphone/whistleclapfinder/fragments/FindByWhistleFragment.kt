package com.findphone.whistleclapfinder.fragments

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.clapsClasess.clapsdatabase.AppDatabase
import com.findphone.whistleclapfinder.fragments.Whistle_And_Claps_Both_Utils.whistleAudioStopAndFlashLightStopAndVibrationStop
import com.findphone.whistleclapfinder.whistleClasess.WhistleAudioAdapter
import com.findphone.whistleclapfinder.whistleClasess.WhistleDetectionService
import com.findphone.whistleclapfinder.whistleClasess.WhistleForegroundService
import com.findphone.whistleclapfinder.whistleClasess.WhistleRecorderThread
import com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.base64ToBitmap
import com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.checkInternet
import com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.imageConverter
import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.AudioModelWhisleDatabase
import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.WhistleAudioModelDao
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class FindByWhistleFragment : Fragment() {

    lateinit var v: View;
    lateinit var ringRecylerview:RecyclerView
    private var database: AppDatabase? = null
    private var audioModelDao: WhistleAudioModelDao? = null


    private var audioAdapter: WhistleAudioAdapter? = null
    lateinit var activeDactiveBtn: LinearLayout
    var isRunOrNot:Boolean = false
    lateinit var icon: ImageView
    lateinit var nameText: TextView
    lateinit var onOffTextView: TextView
//    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var dialog: AlertDialog


    private val PREFS_NAME = "MyPrefsFile"
    private val DATA_ADDED_KEY = "dataAdded"


    private val ChangeIconSelectedBroadcastManagerWhistle: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle the broadcast data here

            val activity: Activity? = activity
            if (activity != null) {

                val sharedPreferences: SharedPreferences = activity.getSharedPreferences("MyPreferencesSelectItem", Context.MODE_PRIVATE)
                val base64Image = sharedPreferences.getString("image_key", "")
                val imageBitmap: Bitmap = base64ToBitmap(base64Image)!!

                icon.setImageBitmap(imageBitmap)
                /* below fun All Oncreate Data Set in OnStart */
                allOnCreateData("ChangeIconSelectedBroadcastManagerWhistle")

            }


        }


    }



    private val onFinish: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle the broadcast data here

            val activity: Activity? = activity
            if (activity != null) {

                isRunOrNot = false

                modeNotActivityLayoutElementsSet()


                // This method is called when the timer finishes (after 10 seconds)

                // etc ...
            }


        }


    }



    var imageBitmap: Bitmap? = null
    var isCheckNot:Boolean = false
    var isCheckInterstitialAd:Boolean = false
    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_find_by_whistle, container, false)

        /* below fun All Oncreate Data Set in OnStart */
        allOnCreateData("")

        return v;
    }

    private fun allOnCreateData(s: String) {

        dialog = IndeterminateProgressDialog(requireActivity())
        dialog.setMessage("Please wait...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        /*  below condition use is when  ChangeIconSelectedBroadcastManagerWhistle is righister then after 100 neno secnd unregisterReceiver and handler into use handler after 100 neno second Re registerReceiver*/
        if (s.equals("ChangeIconSelectedBroadcastManagerWhistle")){
            Handler(Looper.getMainLooper()).postDelayed({

                val activity: Activity? = activity
                if (activity != null){
                    LocalBroadcastManager.getInstance(activity).unregisterReceiver(ChangeIconSelectedBroadcastManagerWhistle);
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Register the receiver in your activity
                        LocalBroadcastManager.getInstance(activity).registerReceiver(ChangeIconSelectedBroadcastManagerWhistle, IntentFilter("ChangeIconSelectedBroadcastManagerWhistle"))

                    }, 100)
                }

            }, 100)
        }


        // Add data first time in room database below fun
        firstTimeDataAdd()

        // Register the receiver in your activity
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(ChangeIconSelectedBroadcastManagerWhistle, IntentFilter("ChangeIconSelectedBroadcastManagerWhistle"))
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(onFinish, IntentFilter("onFinish"))

        ringRecylerview = v.findViewById(R.id.ringRecylerview);
        activeDactiveBtn = v.findViewById(R.id.activeDactiveBtn);
        icon = v.findViewById(R.id.icon);
        nameText = v.findViewById(R.id.nameText);
        onOffTextView = v.findViewById(R.id.onOffTextView);

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("MyPreferencesSelectItem", Context.MODE_PRIVATE)
        val base64Image = sharedPreferences.getString("image_key", "")
        try {
            imageBitmap = base64ToBitmap(base64Image)!!
        }catch (e:NullPointerException){

        }


        if (imageBitmap!=null){
            icon.setImageBitmap(imageBitmap)
        }else{

        }
        val recorderThread = WhistleRecorderThread() // 'this' is the Context from an Activity or Service
        recorderThread.setContext(requireActivity())

        activeDactiveBtn.setOnClickListener {

            // check internet
            if (checkInternet(requireActivity())){

//                mProgressDialog.show()
                dialog.show()


                // make handler if ad not loaded after 7 second dismiss dailog and mInterstitialAd set null
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({

//                    mProgressDialog.dismiss()
                    dialog.dismiss()

                    Log.i(TAG, "Handler");

                    Log.i(ContentValues.TAG, "Handler");
                    if (isCheckInterstitialAd){
                        Log.i(ContentValues.TAG, "Handler isCheckInterstitialAd true");
                    }else{
                        Log.i(ContentValues.TAG, "Handler isCheckInterstitialAd false");

                        /*  service is start */
                        activeDactiveBtnWorkClass()
                        mInterstitialAd = null
                    }
                }, 7000)


                MobileAds.initialize(requireActivity(),
                    OnInitializationCompleteListener { })
                val adRequest: AdRequest = AdRequest.Builder().build()
                InterstitialAd.load(requireActivity(), getString(R.string.interstitial_ad_whistle_and_claps_on_button), adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd

                            Log.i(TAG, "onAdLoaded  ");
                            isCheckInterstitialAd = true
                            // To stop the handler from executing the Runnable:
                            handler.removeCallbacksAndMessages(null)


                            if (isCheckInterstitialAd){
                                Log.i(TAG, "isCheckInterstitialAd  adLoaded");
//                                mProgressDialog.dismiss()
                                dialog.dismiss()


                                mInterstitialAd!!.show(requireActivity())


                                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                                    override fun onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(TAG, "Ad was clicked.")
                                    }

                                    override fun onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        Log.d(TAG, "Ad dismissed fullscreen content.")
                                        mInterstitialAd = null

                                        /*  service is start */
                                        activeDactiveBtnWorkClass()

                                    }

                                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                        // Called when ad fails to show.
                                        Log.e(TAG, "Ad failed to show fullscreen content.")
                                        mInterstitialAd = null
                                    }

                                    override fun onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "Ad recorded an impression.")
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                        Log.d(TAG, "Ad showed fullscreen content.")
                                    }
                                }


                            }else{
                                Log.i(TAG, "isCheckInterstitialAd  adLoaded not load");

                            }


                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Handle the error
                            Log.d(TAG, loadAdError.toString())
                            mInterstitialAd = null
//                            mProgressDialog.dismiss()
                            dialog.dismiss()
                            // To stop the handler from executing the Runnable:
                            handler.removeCallbacksAndMessages(null)

                            /*  service is start */
                            activeDactiveBtnWorkClass()


                        }
                    })


            }else{
                /*  service is start */
                activeDactiveBtnWorkClass()

            }










//            if (isCheckInterstitialAd && checkInternet(requireActivity())){
//
//                if (mInterstitialAd != null) {
//
//
//                } else {
//
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
//
//
//                }
//
//
//            }else{
//
//
//
//            }


        }



        if (!isServiceRunning(WhistleForegroundService::class.java)) {
            Log.d("checksound111wff1111111","Fourground Service is not run")

            modeNotActivityLayoutElementsSet()

        }else{
            Log.d("checksound111wff1111111","Fourground Service is run")

            modeIsActiveLayoutElementsSet()


            activeDactiveBtn.setOnClickListener {

                if (!isCheckNot){

                    val serviceIntent = Intent(requireActivity(), WhistleForegroundService::class.java)
                    val dataToSend = "Stop"
                    serviceIntent.putExtra("action", dataToSend)
                    requireActivity().startService(serviceIntent)

                    val serviceIntent1 = Intent(requireActivity(), WhistleDetectionService::class.java)
                    val dataToSend1 = "Stop"
                    serviceIntent1.putExtra("action", dataToSend1)
                    requireActivity().startService(serviceIntent1)

                    isRunOrNot = false
                    isCheckNot = true


                    modeNotActivityLayoutElementsSet()
                    whistleAudioStopAndFlashLightStopAndVibrationStop(requireActivity()) // all whistle program off means audio stop vibration stop flashlight stop

                }else{


                    /*  service is start */
                    if (!isRunOrNot){
                        Log.d("checksound11111", "if (!isRunOrNot){")

                        val serviceIntent = Intent(requireActivity(), WhistleDetectionService::class.java)
                        val dataToSend = "Start"
                        serviceIntent.putExtra("action", dataToSend)
                        requireActivity().startService(serviceIntent)


                        modeIsActiveLayoutElementsSet()


                        isRunOrNot = true

                    }

                    /*  service is stope */
                    else{

                        // Set the flag to start as a foreground service


                        val serviceIntent = Intent(requireActivity(), WhistleDetectionService::class.java)
                        val dataToSend = "Stop"
                        serviceIntent.putExtra("action", dataToSend)
                        requireActivity().startService(serviceIntent)
                        isRunOrNot = false

                        modeNotActivityLayoutElementsSet()


                    }



                }

            }

        }

    }

    class IndeterminateProgressDialog(context: Context) : AlertDialog(context) {
        private val messageTextView: TextView

        init {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null)
            messageTextView = view.findViewById(R.id.message)
            setView(view)
        }

        override fun setMessage(message: CharSequence?) {
            this.messageTextView.text = message.toString()
        }

    }

    private fun activeDactiveBtnWorkClass() {
        /*  service is start */
        if (!isRunOrNot){

            val serviceIntent = Intent(requireActivity(), WhistleDetectionService::class.java)
            val dataToSend = "Start"
            serviceIntent.putExtra("action", dataToSend)
            requireActivity().startService(serviceIntent)

            modeIsActiveLayoutElementsSet()
            Log.d("checkserviceoff","isRunOrNot = true is check")

            isRunOrNot = true


        }

        /*  service is stope */
        else{

            // Set the flag to start as a foreground service

            val serviceIntent = Intent(requireActivity(), WhistleDetectionService::class.java)
            val dataToSend = "Stop"
            serviceIntent.putExtra("action", dataToSend)
            requireActivity().startService(serviceIntent)

            val serviceIntent1 = Intent(requireActivity(), WhistleForegroundService::class.java)
            val dataToSend1 = "Stop"
            serviceIntent1.putExtra("action", dataToSend1)
            requireActivity().startService(serviceIntent1)

            Log.d("checkserviceoff","isRunOrNot = false is check")

            isRunOrNot = false
            whistleAudioStopAndFlashLightStopAndVibrationStop(requireActivity()) // all whistle program off means audio stop vibration stop flashlight stop


            modeNotActivityLayoutElementsSet()
        }
    }

    private fun createProgressDialog() {
//        mProgressDialog = ProgressDialog(requireActivity())
//        mProgressDialog.setTitle("Please Wait..")
//        mProgressDialog.setMessage("Loading...")


        dialog.setMessage("Please wait...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
    }


    private fun firstTimeDataAdd() {
        val prefs: SharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val dataAdded = prefs.getBoolean(DATA_ADDED_KEY, false)

        if (!dataAdded) {
            // Data has not been added, so add it
            addInitialDataToDatabase()

            Log.d("checkshardprefrence","false")

            // Mark that data has been added
            val editor = prefs.edit()
            editor.putBoolean(DATA_ADDED_KEY, true)
            editor.apply()
        }else{
            Log.d("checkshardprefrence","true")

            database = AppDatabase.getDatabase(activity) as AppDatabase
            audioModelDao = database!!.whistleAudioModelDao()
            Log.d("checkshardprefrence","audioModelDao")

            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        val batterySaving1: ArrayList<AudioModelWhisleDatabase> = audioModelDao!!.allAudioItems() as ArrayList<AudioModelWhisleDatabase>
                        // Update UI on the main thread
                        requireActivity().runOnUiThread(Runnable {

                            val activity: Activity? = activity
                            if (activity!=null){
                                val spanCount = 2 // You can change this to the number of columns you want
                                val layoutManager = GridLayoutManager(requireActivity(), spanCount)
                                ringRecylerview.layoutManager = layoutManager

                                audioAdapter = WhistleAudioAdapter(batterySaving1!!,requireActivity())
                                ringRecylerview.setAdapter(audioAdapter)
                            }

                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            thread.start()

        }

    }

    private fun addInitialDataToDatabase() {

        val initialAudioItems: MutableList<AudioModelWhisleDatabase> = ArrayList()
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.guitar),imageConverter(context, R.drawable.guitar_icon),R.raw.guitar,10000)) // 10000 means 10 second
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.trumpet),imageConverter(context, R.drawable.trumpet_icon),R.raw.trumpet,10000))
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.chicken),imageConverter(context, R.drawable.chicken_icon),R.raw.chicken,10000))
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.ambulance),imageConverter(context, R.drawable.ambulance_icon),R.raw.ambulance,10000))
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.frog),imageConverter(context, R.drawable.frog_icon),R.raw.frog,10000))
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.cat),imageConverter(context, R.drawable.cat_icon),R.raw.cat,10000))
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.dog),imageConverter(context, R.drawable.dog_icon),R.raw.dog,10000))
        initialAudioItems.add(AudioModelWhisleDatabase(true,true,true,10,getString(R.string.scooter),imageConverter(context, R.drawable.scooter_icon),R.raw.scooter,10000))



        database = AppDatabase.getDatabase(activity) as AppDatabase
        audioModelDao = database!!.whistleAudioModelDao()


        for (audioModel in initialAudioItems) {


            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        // insert database
                        audioModelDao!!.insert(audioModel)

                        val batterySaving1: ArrayList<AudioModelWhisleDatabase> = audioModelDao!!.allAudioItems() as ArrayList<AudioModelWhisleDatabase>


                        // Update UI on the main thread
                        requireActivity().runOnUiThread(Runnable {
                            Log.d("checkshardprefrence", "Data add "+batterySaving1.get(0).title)

                            val spanCount = 2 // You can change this to the number of columns you want
                            val layoutManager = GridLayoutManager(requireActivity(), spanCount)
                            ringRecylerview.layoutManager = layoutManager

                            audioAdapter = WhistleAudioAdapter(batterySaving1,requireActivity())
                            ringRecylerview.setAdapter(audioAdapter)


                        })
                    } catch (e: Exception) {
                        // Handle exceptions
                        e.printStackTrace()
                    }
                }
            }
            thread.start()


        }
    }

    private fun modeIsActiveLayoutElementsSet() {

        activeDactiveBtn.background = ContextCompat.getDrawable(requireActivity(),R.drawable.mode_active_layout_bg)
        nameText.text = getString(R.string.tap_to_turn_off)
        val textColor = Color.parseColor("#2D66FA") // Replace with your desired color
        nameText.setTextColor(textColor)

        onOffTextView.text = getString(R.string.on)
        val onOffTextViewtextColor = Color.parseColor("#2D66FA") // Replace with your desired color
        onOffTextView.setTextColor(onOffTextViewtextColor)

        icon.setColorFilter(null); // Remove the grayscale color filter

    }

    private fun modeNotActivityLayoutElementsSet() {


        activeDactiveBtn.background = ContextCompat.getDrawable(requireActivity(),R.drawable.mode_not_active_layout_bg)

        nameText.text = getString(R.string.tap_to_turn_on)
        val textColor = Color.parseColor("#767676") // Replace with your desired color
        nameText.setTextColor(textColor)


        onOffTextView.text = getString(R.string.off)
        val onOffTextViewtextColor = Color.parseColor("#767676") // Replace with your desired color
        onOffTextView.setTextColor(onOffTextViewtextColor)

        // Create a ColorMatrix that converts colors to grayscale
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f) // 0 means fully desaturated (grayscale)
        // Create a ColorMatrixColorFilter with the grayscale ColorMatrix
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        // Apply the color filter to the ImageView
        icon.setColorFilter(colorFilter)

    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }




}