package com.findphone.whistleclapfinder.whistleClasess.whistledatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface WhistleAudioModelDao {
    @Insert
    fun insert(audiomodelwhisledatabase: AudioModelWhisleDatabase?)

    @get:Query("SELECT * FROM audio_items_whistle")
    val allAudioItems: List<AudioModelWhisleDatabase?>?

    @Query("SELECT * FROM audio_items_whistle")
    fun allAudioItems(): List<AudioModelWhisleDatabase?>?

    @Update
    fun updateBatterySaveing(audiomodelwhisledatabase: AudioModelWhisleDatabase?)
}
