package com.findphone.whistleclapfinder.whistleClasess

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils
import com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils.soundTitleTranslator
import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.AudioModelWhisleDatabase
import java.io.ByteArrayOutputStream
import java.util.Locale


class WhistleAudioAdapter(audioModelList: ArrayList<AudioModelWhisleDatabase>, requireActivity: FragmentActivity) :
    RecyclerView.Adapter<WhistleAudioAdapter.ViewHolder>() {
    private val audioModelList: ArrayList<AudioModelWhisleDatabase>
    private var mediaPlayer: MediaPlayer? = null
    private val context: Context
    private var currentPlayingPosition: Int = -1 // Track the currently playing item

    var selecteditem = 0


    init {
        this.audioModelList = audioModelList
        this.context = requireActivity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ring_tune_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val audioModel: AudioModelWhisleDatabase = audioModelList[position]



        // sound title translate language current into current language
       val displayName = soundTitleTranslator(context,audioModel.title)

        holder.nameText.text = displayName
        // Set the image for the ImageView








        val imageBitmap: Bitmap = MyUtils.base64ToBitmap(audioModel.imageResourceId)!!
        holder.icon.setImageBitmap(imageBitmap)

        // Handle play/pause button click
        holder.playPauseButton.setOnClickListener { view ->
            if (audioModel.isPlaying) {
                pauseAudio()
                audioModel.isPlaying = false
                currentPlayingPosition = -1 // No item is playing
            } else {
                if (currentPlayingPosition != -1 && currentPlayingPosition != position) {
                    // Stop the previous audio if one is playing
                    val previousAudioModel = audioModelList[currentPlayingPosition]
                    previousAudioModel.isPlaying = false
                    notifyItemChanged(currentPlayingPosition)
                }
                val audioResourceId: Int = context.resources.getIdentifier(audioModel.title, "raw", context.packageName)
                playAudio(audioResourceId)
                audioModel.isPlaying = true
                currentPlayingPosition = position
            }
            notifyItemChanged(position)
        }

        // Update play/pause button state based on the audio item's status
        holder.playPauseButton.setImageResource(
            if (audioModel.isPlaying) R.drawable.song_pause_icon else R.drawable.song_play_icon
        )



        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferencesSelectItem", Context.MODE_PRIVATE)
        val nameTitle = sharedPreferences.getString("nameTitle", "")


        if (nameTitle!=null){
            if(nameTitle.equals(audioModel.title)){
                holder.checkButton.setVisibility(View.VISIBLE);
                holder.mainLayout.background = ContextCompat.getDrawable(context,R.drawable.ring_item_border_color_bg)

                Log.d("bitmapbitmap", "VISIBLE   "+audioModel.title);
            } else{
                holder.checkButton.setVisibility(View.INVISIBLE);
                holder.mainLayout.background = ContextCompat.getDrawable(context,R.drawable.ring_item_bg)
                Log.d("bitmapbitmap", "INVISIBLE");

            }
        }else{

        }

        // USE BELOW FUNCTION BECUSE USE COME FIRST TIME IN APP THEN BY DEFUALT ONE AUDIO ITEM SELECTED
        val prefs: SharedPreferences = context.getSharedPreferences("BY_DEFUALT_FIRST_TIME_ITEM_SET", Context.MODE_PRIVATE)
        val dataAdded = prefs.getBoolean("BY_DEFUALT_DATA_ADDED_KEY", false)

        if (!dataAdded){

            val editor_BY_DEFUALT_DATA_ADDED_KEY = prefs.edit()
            editor_BY_DEFUALT_DATA_ADDED_KEY.putBoolean("BY_DEFUALT_DATA_ADDED_KEY", true)
            editor_BY_DEFUALT_DATA_ADDED_KEY.apply()

            val audioResourceId: Int = context.resources.getIdentifier(audioModel.title, "raw", context.packageName) // set audio audioResourceId means complete path audio


            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferencesSelectItem", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("image_key", audioModel.imageResourceId)
            editor.putString("nameTitle", audioModel.title)
            editor.putBoolean("flashLight", audioModel.isFlashLight)
            editor.putBoolean("isVibration", audioModel.isVibration)
            editor.putInt("soundSeekbar", audioModel.soundSeekbar)
            editor.putBoolean("isSound", audioModel.isSound)
            editor.putInt("timeDurationService", audioModel.timeDurationService)
            editor.putBoolean("isRingTuneSelected", true)
            editor.putInt("audioResourceId", audioResourceId)
            editor.apply()

            // LocalBroadcastManager use for refresh icon in on off button
            val intent = Intent("ChangeIconSelectedBroadcastManagerWhistle")
            intent.putExtra("key", "My Data")
            // put your all data using put extra
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

            pauseAudio()
            audioModel.isPlaying = false


        }else{

        }


        holder.mainLayout.setOnClickListener {

            val audioResourceId: Int = context.resources.getIdentifier(audioModel.title, "raw", context.packageName) // set audio audioResourceId means complete path audio


            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferencesSelectItem", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("image_key", audioModel.imageResourceId)
            editor.putString("nameTitle", audioModel.title)
            editor.putBoolean("flashLight", audioModel.isFlashLight)
            editor.putBoolean("isVibration", audioModel.isVibration)
            editor.putInt("soundSeekbar", audioModel.soundSeekbar)
            editor.putBoolean("isSound", audioModel.isSound)
            editor.putInt("timeDurationService", audioModel.timeDurationService)
            editor.putBoolean("isRingTuneSelected", true)
            editor.putInt("audioResourceId", audioResourceId)
            editor.apply()

            // LocalBroadcastManager use for refresh icon in on off button
            val intent = Intent("ChangeIconSelectedBroadcastManagerWhistle")
            intent.putExtra("key", "My Data")
            // put your all data using put extra
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

            pauseAudio()
            audioModel.isPlaying = false


//            notifyDataSetChanged();
            notifyItemChanged(position)



        }

        holder.subSettingBtn.setOnClickListener {

            Log.d("bitmapbitmap1111", "subSettingBtn click   "+audioModel.audioResourceId);

//            val intent = Intent(context,SubWhistleSettingActivity::class.java)
//            intent.putExtra("image",audioModel.imageResourceId)
//            context.startActivity(intent)

            Log.d("checkid", "WhistleAudioAdapter   "+audioModel.id)

            val intent = Intent(context, SubWhistleSettingActivity::class.java)
            intent.putExtra("audioModelByteArray", audioModelList)
            intent.putExtra("WhistleAudioAdapter_Id", audioModel.id)
            intent.putExtra("position", position)
            context.startActivity(intent)

            val activity: AppCompatActivity = context as AppCompatActivity

            activity.overridePendingTransition(R.anim.enter, R.anim.exit)

        }



    }

    fun getTranslatedName(context: Context, language: String, nameKey: String): String {
        val resources = context.resources
        val resourceId = resources.getIdentifier(nameKey, "string", context.packageName)
        return try {
            if (resourceId != 0) {
                val locale = Locale(language)
                val configuration = Configuration(resources.configuration)
                configuration.setLocale(locale)
                val localizedResources = context.createConfigurationContext(configuration).resources
                localizedResources.getString(resourceId) // Use getString from localizedResources
            } else {
                nameKey // Return the key if no translation is found
            }
        } catch (e: Exception) {
            nameKey // Return the key in case of an error
        }
    }




    fun getBitmapFromDrawable(context: Context, drawableResourceId: Int): Bitmap? {
        val drawable: Drawable? = ContextCompat.getDrawable(context, drawableResourceId)

        if (drawable != null) {
            // Check if the drawable is a BitmapDrawable
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }

            // For other drawable types, you can create a Bitmap and draw the drawable onto it
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }

        return null
    }

        fun bitmapToBase64(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }


    override fun getItemCount(): Int {
        return audioModelList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var playPauseButton: ImageView
        var icon: ImageView
        var checkButton: ImageView
        var nameText: TextView
        var mainLayout: LinearLayout
        var subSettingBtn: LinearLayout

        init {
            subSettingBtn = itemView.findViewById(R.id.subSettingBtn)
            mainLayout = itemView.findViewById(R.id.mainLayout)
            checkButton = itemView.findViewById(R.id.checkButton)
            playPauseButton = itemView.findViewById(R.id.playPauseButton)
            icon = itemView.findViewById(R.id.icon)
            nameText = itemView.findViewById(R.id.nameText)
        }
    }

    // Add methods for playing and pausing audio here
    // You'll need to manage MediaPlayer state and audio playback.

    private fun playAudio(audioResourceId: Int) {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }

        mediaPlayer = MediaPlayer.create(context, audioResourceId)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            // Release the MediaPlayer when playback completes
            mediaPlayer?.release()
            mediaPlayer = null
            audioModelList[currentPlayingPosition].isPlaying = false
            notifyItemChanged(currentPlayingPosition)
            currentPlayingPosition = -1
        }
    }

    private fun pauseAudio() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.pause()
        }
    }
}
