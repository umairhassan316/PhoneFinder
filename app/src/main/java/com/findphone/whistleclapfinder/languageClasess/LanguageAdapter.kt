package com.findphone.whistleclapfinder.languageClasess

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.findphone.whistleclapfinder.R
import com.findphone.whistleclapfinder.activity.HomeActivity
import com.findphone.whistleclapfinder.SetionClasss.LanguageHelper

class LanguageAdapter(private val saveBtn: ImageView, private val context: Context, private val languages: List<LanguageModel>, private val onItemClickListener: (LanguageModel) -> Unit) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    var languageCode:String = ""
    var lanugageNameCheck:String = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.language_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val language = languages[position]
        holder.bind(language)
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val languageName = itemView.languageName
//        private val radioButton = itemView.radioButton

        var languageIcon: ImageView
        var languageName: TextView
        var radioButton: RadioButton
        var languageItemMianLayout: LinearLayout


        init {
            languageIcon = itemView.findViewById(R.id.languageIcon)
            languageName = itemView.findViewById(R.id.languageName)
            radioButton = itemView.findViewById(R.id.radioBtn)
            languageItemMianLayout = itemView.findViewById(R.id.languageItemMianLayout)

        }





        fun bind(language: LanguageModel) {
            languageName.text = language.name
            languageIcon.setImageResource(language.flagResource)


            val selecteLanguageCode = LanguageHelper.loadLocale(context)

            if (selecteLanguageCode!=null || languageCode!=null){

                if (languageCode == ""){

                    if(selecteLanguageCode.equals(language.code)){
                        languageItemMianLayout.background = ContextCompat.getDrawable(context,R.drawable.language_selected_item_bg)
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.white))
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.black))


                        radioButton.isChecked = true

                        Log.d("bitmapbitmap", "VISIBLE   " + language.code);
                    } else{
                        languageItemMianLayout.background = ContextCompat.getDrawable(context,R.drawable.language_unselected_item_bg)
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.black))
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.headingtextcolor))



                        Log.d("bitmapbitmap", "INVISIBLE");
                        radioButton.isChecked = false

                    }



                }else{

                    if(languageCode.equals(language.code)){
                        languageItemMianLayout.background = ContextCompat.getDrawable(context,R.drawable.language_selected_item_bg)
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.white))
                        radioButton.setTextColor(ContextCompat.getColor(context,R.color.white))
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.black))

                        radioButton.isChecked = true

                        Log.d("bitmapbitmap", "VISIBLE   " + language.code);
                    } else{
                        languageItemMianLayout.background = ContextCompat.getDrawable(context,R.drawable.language_unselected_item_bg)
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.black))
                        languageName.setTextColor(ContextCompat.getColor(context,R.color.headingtextcolor))
                        Log.d("bitmapbitmap", "INVISIBLE");
                        radioButton.isChecked = false

                    }


                }





            }


            radioButton.setOnClickListener {


                languageCode = language.code
                lanugageNameCheck = language.name
                val context = context as AppCompatActivity

                context.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

                Log.d("checklanguagename", language.name)


                notifyDataSetChanged()

            }

            itemView.setOnClickListener {


                languageCode = language.code
                lanugageNameCheck = language.name
                val context = context as AppCompatActivity

                context.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

                Log.d("checklanguagename", language.name)


                notifyDataSetChanged()

//                onItemClickListener(language)
//                // Uncheck other radio buttons
//                for (lang in languages) {
//                    lang.selected = lang == language
//                }

            }

            saveBtn.setOnClickListener {

                if (lanugageNameCheck.equals("")){
                    val languageNameGet = LanguageHelper.getMyPrefsLanguageName(context)
                    setLocale(context, languageCode, languageNameGet!!)
                }else{
                    setLocale(context, languageCode, lanugageNameCheck)
                }

            }



            Log.d("bitmapbitmap", "checkBtn click  " + languageCode);
            }


        }

    private fun setLocale(context: Context, languageCode: String, languageName: String) {

        LanguageHelper.setLocale(context, languageCode)  // SET LANGUAGE FUNCTION
        LanguageHelper.addMyPrefsLanguageName(context,languageName) // SET LANGUAGE NAME FUNCTION
        Log.d("checklanguagename",languageName)


        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)

    }
}