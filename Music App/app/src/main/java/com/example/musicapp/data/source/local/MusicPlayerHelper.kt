package com.example.musicapp.data.source.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.musicapp.data.model.Song
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class MusicPlayerHelper(val context: Context) {

    fun fetchSongFromStorage(): MutableList<Song> {
        val songList = mutableListOf<Song>()
        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val audioCursor = context.contentResolver.query(contentUri, proj, null, null, null)

        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    val audioId = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val audioTitle = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val audioArtist =
                        audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val audioDuration =
                        audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val audioData = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val albumId = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val album = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val duration = audioCursor.getString(audioDuration)
                    if (duration != null) {
                        val song = Song()
                        song.id = audioId
                        song.name = audioCursor.getString(audioTitle)
                        song.singer = audioCursor.getString(audioArtist)
                        song.path = audioCursor.getString(audioData)
                        song.duration = duration.toInt()
//                        song.albumId = audioCursor.getLong(albumId)
//                        song.album = audioCursor.getString(album)
                        songList.add(song)
                    }
                } while (audioCursor.moveToNext())
            }
        }
        return songList
    }

    fun parseLongToTime(timeLong: Int): String {
        var time = ""
        time = SimpleDateFormat("mm:ss").format(Date(timeLong.toLong()))
        return time
    }

    fun getBitmapSong(path: String): Bitmap? {
        val mmr = MediaMetadataRetriever()
        val uri = Uri.parse(path).toString()
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                mmr.setDataSource(uri, HashMap<String, String>())
            } catch (ex: RuntimeException) {
                // something went wrong with the file, ignore it and continue
                ex.printStackTrace()
            }
        } else {
            mmr.setDataSource(uri)
        }
        val byteImage = mmr.embeddedPicture
        if (byteImage != null) {
            return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.size)
        }
        return null
    }
}
