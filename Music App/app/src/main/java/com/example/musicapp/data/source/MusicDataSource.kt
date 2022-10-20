package com.example.musicapp.data.source

import com.example.musicapp.data.source.local.OnResultCallback

interface MusicDataSource {
    fun getData(callback: OnResultCallback)
}
