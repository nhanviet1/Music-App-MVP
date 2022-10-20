package com.example.musicapp.data.repository

import com.example.musicapp.data.source.MusicDataSource
import com.example.musicapp.data.source.local.OnResultCallback


class SongRepository private constructor(private val musicDataSource: MusicDataSource) :
    MusicDataSource {

    override fun getData(callback: OnResultCallback) {
        musicDataSource.getData(callback)
    }

    companion object {
        private var instance: SongRepository? = null
        fun getInstance(musicDataSource: MusicDataSource) = synchronized(this) {
            instance ?: SongRepository(musicDataSource).also { instance = it }
        }
    }
}
