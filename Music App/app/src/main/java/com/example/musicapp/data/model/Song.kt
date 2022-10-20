package com.example.musicapp.data.model

import java.io.Serializable
data class Song(
    var id: Int = 0,
    var name: String = "",
    var singer: String = "",
    var path: String = "",
    var duration: Int = 0,
//    var albumId: Long = 0,
//    var album: String = ""
): Serializable

data class ListSong(
    val list: List<Song>
): Serializable