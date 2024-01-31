package com.findphone.whistleclapfinder.fragments

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
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
import com.findphone.whistleclapfinder.clapsClasess.ClapsAudioAdapter
import com.findphone.whistleclapfinder.clapsClasess.ClapsDetectionService
import com.findphone.whistleclapfinder.clapsClasess.ClapsForegroundService
import com.findphone.whistleclapfinder.clapsClasess.ClapsRecorderThread
import com.findphone.whistleclapfinder.clapsClasess.clapsdatabase.AppDatabase
import com.findphone.whistleclapfinder.clapsClasess.clapsdatabase.AudioModelClapsDatabase
import com.findphone.whistleclapfinder.clapsClasess.clapsdatabase.AudioModelDao
import com.findphone.whistleclapfinder.clapsClasess.utils.MyUtils
import com.findphone.whistleclapfinder.clapsClasess.utils.MyUtils.base64ToBitmap
import com.findphone.whistleclapfinder.whistleClasess.WhistleDetectionService
import com.findphone.whistleclapfinder.whistleClasess.WhistleForegroundService
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


// TODO: Rename parameter arguments, choose names that match

class FindByClapFragment : Fragment() {

    lateinit var v: View;
    private var database: AppDatabase? = null
    private var audioModelDao: AudioModelDao? = null


    lateinit var ringRecylerview: RecyclerView

    private var audioAdapter: ClapsAudioAdapter? = null
    lateinit var activeDactiveBtn: LinearLayout
    var isRunOrNot:Boolean = false
    lateinit var icon: ImageView
    lateinit var nameText: TextView


    var lbm: LocalBroadcastManager? = null
    private val PREFS_NAME = "MyPrefsFileClaps"
    private val DATA_ADDED_KEY = "dataAdded"


    var isCheckNot:Boolean = false
//    private lateinit var mProgressDialog: ProgressDialog
        private lateinit var dialog: AlertDialog

    private val ChangeIconSelectedBroadcastManagerClaps: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle the broadcast data here

            Log.d("bitmapbitmap", "1")

            val activity: Activity? = activity
            if (activity != null){

                val sharedPreferences: SharedPreferences = activity.getSharedPreferences("MyPreferencesSelectItemClaps", Context.MODE_PRIVATE)
                val base64Image = sharedPreferences.getString("image_key", "")
                val imageBitmap: Bitmap = base64ToBitmap(base64Image)!!

                icon.setImageBitmap(imageBitmap)
                /* below fun All Oncreate Data Set in OnStart */
                allOnCreateData("ChangeIconSelectedBroadcastManagerClaps")

            }


        }


    }

    private val receiver11: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Handle the broadcast data here

            Log.d("bitmapbitmap", "1")

            val activity: Activity? = activity
            if (activity != null) {

                isRunOrNot = false
                modeNotActivityLayoutElementsSet()

                // This method is called when the timer finishes (after 10 seconds)
                Log.d("checksound1111111111", "onFinish 2")

                // etc ...
            }


        }


    }

    var imageBitmap: Bitmap? = null
    lateinit var onOffTextView: TextView
    var isCheckInterstitialAd:Boolean = false
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_find_by_clap, container, false)


        /* below fun All Oncreate Data Set in OnStart */
        allOnCreateData("")

        return v;
    }

    private fun allOnCreateData(s: String) {


        dialog = FindByWhistleFragment.IndeterminateProgressDialog(requireActivity())
        dialog.setMessage("Please wait...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)



        /*  below condition use is when  ChangeIconSelectedBroadcastManagerClaps is righister then after 100 neno secnd unregisterReceiver and handler into use handler after 100 neno second Re registerReceiver*/
        if (s.equals("ChangeIconSelectedBroadcastManagerClaps")){
            Handler(Looper.getMainLooper()).postDelayed({

                Log.d("bitmapbitmap111", "ChangeIconSelectedBroadcastManagerWhistle  "+"Handler")

                val activity: Activity? = activity
                if (activity != null){
                    LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(ChangeIconSelectedBroadcastManagerClaps);

                    Handler(Looper.getMainLooper()).postDelayed({

                        Log.d("bitmapbitmap111", "ChangeIconSelectedBroadcastManagerWhistle  "+"Handler")

                        // Register the receiver in your activity
                        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(ChangeIconSelectedBroadcastManagerClaps, IntentFilter("ChangeIconSelectedBroadcastManagerClaps"))

                    }, 100)
                }



            }, 100)
        }



        // Add data first time in room database below fun
        firstTimeDataAdd()


        // Register the receiver in your activity
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(ChangeIconSelectedBroadcastManagerClaps, IntentFilter("ChangeIconSelectedBroadcastManagerClaps"))
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(receiver11, IntentFilter("onFinish"))

        ringRecylerview = v.findViewById(R.id.ringRecylerview);
        activeDactiveBtn = v.findViewById(R.id.activeDactiveBtn);
        icon = v.findViewById(R.id.icon);
        nameText = v.findViewById(R.id.nameText);
        onOffTextView = v.findViewById(R.id.onOffTextView);

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("MyPreferencesSelectItemClaps", Context.MODE_PRIVATE)
        val base64Image = sharedPreferences.getString("image_key", "")
        try {
            imageBitmap = base64ToBitmap(base64Image)!!
        }catch (e:NullPointerException){

        }


        if (imageBitmap!=null){
            icon.setImageBitmap(imageBitmap)
        }else{

        }
        val recorderThread = ClapsRecorderThread() // 'this' is the Context from an Activity or Service
        recorderThread.setContext(requireActivity())

        activeDactiveBtn.setOnClickListener {

            // check internet
            if (com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.checkInternet(requireActivity())){

//                mProgressDialog.show()
                dialog.show()

                // make handler if ad not loaded after 7 second dismiss dailog and mInterstitialAd set null
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({

//                    mProgressDialog.dismiss()
                    dialog.dismiss()
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


                MobileAds.initialize(requireActivity(), OnInitializationCompleteListener { })
                val adRequest: AdRequest = AdRequest.Builder().build()
                InterstitialAd.load(requireActivity(), getString(R.string.interstitial_ad_whistle_and_claps_on_button), adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd

                            Log.i(ContentValues.TAG, "onAdLoaded  ");
                            isCheckInterstitialAd = true


                            if (isCheckInterstitialAd){
                                Log.i(ContentValues.TAG, "isCheckInterstitialAd  adLoaded");
                                // To stop the handler from executing the Runnable:
                                handler.removeCallbacksAndMessages(null)
//                                mProgressDialog.dismiss()
                                dialog.dismiss()
                                mInterstitialAd!!.show(requireActivity())


                                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                                    override fun onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(ContentValues.TAG, "Ad was clicked.")
                                    }

                                    override fun onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                                        mInterstitialAd = null


                                        /*  service is start */
                                        activeDactiveBtnWorkClass()

                                    }

                                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                        // Called when ad fails to show.
                                        Log.e(ContentValues.TAG, "Ad failed to show fullscreen content.")
                                        mInterstitialAd = null
                                    }

                                    override fun onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(ContentValues.TAG, "Ad recorded an impression.")
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                        Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                                    }
                                }


                            }else{
                                Log.i(ContentValues.TAG, "isCheckInterstitialAd  adLoaded not load");

                            }


                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            // Handle the error
                            Log.d(ContentValues.TAG, loadAdError.toString())
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

        }



        if (!isServiceRunning(ClapsForegroundService::class.java)) {
            Log.d("checksound111111111","Fourground Service is not run")
            modeNotActivityLayoutElementsSet()

        }else{
            Log.d("checksound111111111","Fourground Service is run")

            modeIsActiveLayoutElementsSet()

            activeDactiveBtn.setOnClickListener {

                if (!isCheckNot){

                    Log.d("checksound111111111", "else (isRunOrNot){ 11111111")


                    val serviceIntent = Intent(requireActivity(), ClapsForegroundService::class.java)
                    val dataToSend = "Stop"
                    serviceIntent.putExtra("action", dataToSend)
                    requireActivity().startService(serviceIntent)

                    val serviceIntent1 = Intent(requireActivity(), ClapsDetectionService::class.java)
                    val dataToSend1 = "Stop"
                    serviceIntent1.putExtra("action", dataToSend1)
                    requireActivity().startService(serviceIntent1)


                    isRunOrNot = false
                    isCheckNot = true


                    modeNotActivityLayoutElementsSet()
                    Whistle_And_Claps_Both_Utils.clapsAudioStopAndFlashLightStopAndVibrationStop(requireActivity())
                }else{


                    /*  service is start */
                    if (!isRunOrNot){
                        Log.d("checksound11111", "if (!isRunOrNot){")


                        val serviceIntent = Intent(requireActivity(), ClapsDetectionService::class.java)
                        val dataToSend = "Start"
                        serviceIntent.putExtra("action", dataToSend)
                        requireActivity().startService(serviceIntent)

                        modeIsActiveLayoutElementsSet()

                        isRunOrNot = true

                    }

                    /*  service is stope */
                    else{

                        Log.d("checksound11111", "else (isRunOrNot){")

                        // Set the flag to start as a foreground service


                        val serviceIntent = Intent(requireActivity(), ClapsDetectionService::class.java)
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

    private fun activeDactiveBtnWorkClass() {


        /*  service is start */
        if (!isRunOrNot){
            Log.d("checksound11111", "if (!isRunOrNot){")



//                val intent = Intent(requireActivity(), DetectionService::class.java)
//                requireActivity().startService(intent)

            val serviceIntent = Intent(requireActivity(), ClapsDetectionService::class.java)
            val dataToSend = "Start"
            serviceIntent.putExtra("action", dataToSend)
            requireActivity().startService(serviceIntent)


            modeIsActiveLayoutElementsSet()

            isRunOrNot = true

        }

        /*  service is stope */
        else{

            Log.d("checksound11111", "else (isRunOrNot){")

            // Set the flag to start as a foreground service


            val serviceIntent = Intent(requireActivity(), ClapsDetectionService::class.java)
            val dataToSend = "Stop"
            serviceIntent.putExtra("action", dataToSend)
            requireActivity().startService(serviceIntent)


            val serviceIntent1 = Intent(requireActivity(), ClapsForegroundService::class.java)
            val dataToSend1 = "Stop"
            serviceIntent1.putExtra("action", dataToSend1)
            requireActivity().startService(serviceIntent1)



            isRunOrNot = false
            Whistle_And_Claps_Both_Utils.clapsAudioStopAndFlashLightStopAndVibrationStop(requireActivity())



            modeNotActivityLayoutElementsSet()

        }


    }


    private fun firstTimeDataAdd() {
        val prefs: SharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dataAdded = prefs.getBoolean(DATA_ADDED_KEY, false)

        if (!dataAdded) {
            // Data has not been added, so add it
            addInitialDataToDatabase()

            Log.d("checkshardprefrence","claps false")



            // Mark that data has been added
            val editor = prefs.edit()
            editor.putBoolean(DATA_ADDED_KEY, true)
            editor.apply()
        }else{
            Log.d("checkshardprefrence","claps true")

            database = AppDatabase.getDatabase(activity) as AppDatabase
            audioModelDao = database!!.audioModelDao()
            Log.d("checkshardprefrence","audioModelDao")

            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        val batterySaving1: ArrayList<AudioModelClapsDatabase> = audioModelDao!!.allAudioItems() as ArrayList<AudioModelClapsDatabase>

                        // Update UI on the main thread
                        requireActivity().runOnUiThread(Runnable {

                            val activity: Activity? = activity
                            if (activity!=null){

                                val spanCount = 2 // You can change this to the number of columns you want
                                val layoutManager = GridLayoutManager(requireActivity(), spanCount)
                                ringRecylerview.layoutManager = layoutManager

                                audioAdapter = ClapsAudioAdapter(batterySaving1,requireActivity())
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

        Log.d("checksound111111111", "claps addInitialDataToDatabase")



        val initialAudioItems: MutableList<AudioModelClapsDatabase> = ArrayList()
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.guitar),MyUtils.imageConverter(context, R.drawable.guitar_icon),R.raw.guitar,10000)) // 10000 means 10 second
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.trumpet),MyUtils.imageConverter(context, R.drawable.trumpet_icon),R.raw.trumpet,10000))
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.chicken),MyUtils.imageConverter(context, R.drawable.chicken_icon),R.raw.chicken,10000))
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.ambulance),MyUtils.imageConverter(context, R.drawable.ambulance_icon),R.raw.ambulance,10000))
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.frog),MyUtils.imageConverter(context, R.drawable.frog_icon),R.raw.frog,10000))
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.cat),MyUtils.imageConverter(context, R.drawable.cat_icon),R.raw.cat,10000))
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.dog),MyUtils.imageConverter(context, R.drawable.dog_icon),R.raw.dog,10000))
        initialAudioItems.add(AudioModelClapsDatabase(true,true,true,10,getString(R.string.scooter),MyUtils.imageConverter(context, R.drawable.scooter_icon),R.raw.scooter,10000))




        database = AppDatabase.getDatabase(activity) as AppDatabase
        audioModelDao = database!!.audioModelDao()


        for (audioModel in initialAudioItems) {


            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        // insert database
                        audioModelDao!!.insert(audioModel)
                        Log.d("checksound111111111", "claps insert")

                        val batterySaving1: ArrayList<AudioModelClapsDatabase> = audioModelDao!!.allAudioItems() as ArrayList<AudioModelClapsDatabase>




                        // Update UI on the main thread
                        requireActivity().runOnUiThread(Runnable {
                            Log.d("checkshardprefrence", "Data add "+batterySaving1.get(0).title)

                            val spanCount = 2 // You can change this to the number of columns you want
                            val layoutManager = GridLayoutManager(requireActivity(), spanCount)
                            ringRecylerview.layoutManager = layoutManager




                            audioAdapter = ClapsAudioAdapter(batterySaving1!!,requireActivity())
                            ringRecylerview.setAdapter(audioAdapter)





                        })
                    } catch (e: Exception) {
                        // Handle exceptions
                        Log.d("checksound111111111", "Exception  "+e.message)
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