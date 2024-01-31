package com.findphone.whistleclapfinder.clapsClasess

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.media.MediaPlayer
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
import com.findphone.whistleclapfinder.clapsClasess.clapsdatabase.AudioModelClapsDatabase
import com.findphone.whistleclapfinder.clapsClasess.utils.MyUtils


class ClapsAudioAdapter(audioModelList: ArrayList<AudioModelClapsDatabase>, requireActivity: FragmentActivity) :
    RecyclerView.Adapter<ClapsAudioAdapter.ViewHolder>() {
    private val audioModelList: ArrayList<AudioModelClapsDatabase>
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
        val audioModel: AudioModelClapsDatabase = audioModelList[position]



        // sound title translate language current into current language
        val displayName = MyUtils.soundTitleTranslator(context, audioModel.title)

        holder.nameText.text = displayName
        // Set the image for the ImageView



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



        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferencesSelectItemClaps", Context.MODE_PRIVATE)
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
        val prefs: SharedPreferences = context.getSharedPreferences("BY_DEFUALT_CLAPS_FIRST_TIME_ITEM_SET", Context.MODE_PRIVATE)
        val dataAdded = prefs.getBoolean("BY_DEFUALT_CLAPS_DATA_ADDED_KEY", false)

        if (!dataAdded){

            val editor_BY_DEFUALT_DATA_ADDED_KEY = prefs.edit()
            editor_BY_DEFUALT_DATA_ADDED_KEY.putBoolean("BY_DEFUALT_CLAPS_DATA_ADDED_KEY", true)
            editor_BY_DEFUALT_DATA_ADDED_KEY.apply()

            val audioResourceId: Int = context.resources.getIdentifier(audioModel.title, "raw", context.packageName) // set audio audioResourceId means complete path audio


            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferencesSelectItemClaps", Context.MODE_PRIVATE)
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
            val intent = Intent("ChangeIconSelectedBroadcastManagerClaps")
            intent.putExtra("key", "My Data")
            // put your all data using put extra
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)


            pauseAudio()
            audioModel.isPlaying = false


        }else{

        }





        holder.mainLayout.setOnClickListener {

            val audioResourceId: Int = context.resources.getIdentifier(audioModel.title, "raw", context.packageName) // set audio audioResourceId means complete path audio


            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferencesSelectItemClaps", Context.MODE_PRIVATE)
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
            val intent = Intent("ChangeIconSelectedBroadcastManagerClaps")
            intent.putExtra("key", "My Data")
            // put your all data using put extra
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)


            pauseAudio()
            audioModel.isPlaying = false

            notifyItemChanged(position)



        }

        holder.subSettingBtn.setOnClickListener {

            Log.d("bitmapbitmap1111", "subSettingBtn click   "+audioModel.audioResourceId);

//            val intent = Intent(context,SubWhistleSettingActivity::class.java)
//            intent.putExtra("image",audioModel.imageResourceId)
//            context.startActivity(intent)


            val intent = Intent(context, SubClapsSettingActivity::class.java)
            intent.putExtra("audioModelByteArray", audioModelList)
            intent.putExtra("ClapsAudioAdapter_Id", audioModel.id)
            intent.putExtra("position", position)
            context.startActivity(intent)
            val activity: AppCompatActivity = context as AppCompatActivity

            activity.overridePendingTransition(R.anim.enter, R.anim.exit)

        }


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
