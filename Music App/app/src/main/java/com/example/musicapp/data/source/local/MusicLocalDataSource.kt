package com.example.musicapp.data.source.local

import android.content.Context
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.MusicDataSource


class MusicLocalDataSource(val context: Context) : MusicDataSource {
    override fun getData(listener: OnResultCallback) {
        val songList = MusicPlayerHelper(context).fetchSongFromStorage()
        if (songList.size > 0) {
            listener.onDataLoaded(songList)
        } else {
            listener.onFailed()
        }
    }

    companion object {
        private var instance: MusicLocalDataSource? = null

        fun getInstance(context: Context) = synchronized(this) {
            instance ?: MusicLocalDataSource(context).also { instance = it }
        }
    }

}
