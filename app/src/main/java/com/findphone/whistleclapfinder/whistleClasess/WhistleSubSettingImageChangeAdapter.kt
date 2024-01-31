package com.findphone.whistleclapfinder.whistleClasess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.whistleClasess.utils.MyUtils
import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.AudioModelWhisleDatabase


class WhistleSubSettingImageChangeAdapter(
    whistleItemClickListener: WhistleItemClickListener,
    audioModelList: ArrayList<AudioModelWhisleDatabase>,
    WhistleAudioAdapter_Id: Int,
    requireActivity: FragmentActivity
) :
    RecyclerView.Adapter<WhistleSubSettingImageChangeAdapter.ViewHolder>() {
    private val audioModelList: ArrayList<AudioModelWhisleDatabase>
    private var WhistleAudioAdapter_Id: Int?=null
    private var imageResourceIdClickSet: Int?=null

    private var mediaPlayer: MediaPlayer? = null
    private val context: Context
    private var currentPlayingPosition: Int = -1 // Track the currently playing item

    var selecteditem = 0
    private var whistleItemClickListener: WhistleItemClickListener? = null


    init {
        this.audioModelList = audioModelList
        this.context = requireActivity
        this.WhistleAudioAdapter_Id = WhistleAudioAdapter_Id
        this.whistleItemClickListener = whistleItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.change_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val changeImageModel: AudioModelWhisleDatabase = audioModelList[position]
        // Set the image for the ImageView
        val imageBitmap: Bitmap = MyUtils.base64ToBitmap(changeImageModel.imageResourceId)!!
        holder.icon.setImageBitmap(imageBitmap)


        if (imageResourceIdClickSet!=null){
            if (imageResourceIdClickSet == changeImageModel.id){
                holder.mainLayout.background = ContextCompat.getDrawable(context,R.drawable.sub_setting_image_layout_bg)
                whistleItemClickListener?.onItemClick(changeImageModel.id,changeImageModel.imageResourceId,changeImageModel)
            }else{
                holder.mainLayout.background = ContextCompat.getDrawable(context,R.drawable.sub_setting_image_unselected_layout_bg)
            }
        }else{
            if (WhistleAudioAdapter_Id == changeImageModel.id){
                holder.mainLayout.background = ContextCompat.getDrawable(context,R.drawable.sub_setting_image_layout_bg)
                whistleItemClickListener?.onItemClick(changeImageModel.id,changeImageModel.imageResourceId,changeImageModel)
                Log.d("checkid", "111111111")
            }else{

                holder.mainLayout.background = ContextCompat.getDrawable(context,R.drawable.sub_setting_image_unselected_layout_bg)
                Log.d("checkid", "222222222   "+WhistleAudioAdapter_Id)
            }
        }



        holder.mainLayout.setOnClickListener {



            whistleItemClickListener?.onItemClick(changeImageModel.id,changeImageModel.imageResourceId,changeImageModel)
            Log.d("checkid", " 1  "+changeImageModel.id)


            imageResourceIdClickSet = changeImageModel.id
//
    notifyDataSetChanged()

        }



    }


    override fun getItemCount(): Int {
        return audioModelList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView
        var mainLayout:LinearLayout


        init {

            icon = itemView.findViewById(R.id.icon)
            mainLayout = itemView.findViewById(R.id.mainLayout)

        }
    }

}
