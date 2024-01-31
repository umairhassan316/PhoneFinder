package com.findphone.whistleclapfinder.clapsClasess.clapsdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface AudioModelDao {
    @Insert
    fun insert(audiomodelwhisledatabase: AudioModelClapsDatabase?)

    @get:Query("SELECT * FROM audio_items_claps")
    val allAudioItems: List<AudioModelClapsDatabase?>?

    @Query("SELECT * FROM audio_items_claps")
    fun allAudioItems(): List<AudioModelClapsDatabase?>?

    @Update
    fun updateBatterySaveing(audiomodelclapsdatabase: AudioModelClapsDatabase?)
}
