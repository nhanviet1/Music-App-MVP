package com.example.musicapp.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.databinding.SongItemBinding
import com.example.mvpmusicapp.ui.convertToDurationFormat

class SongAdapter(private val context: Context, private val onClickItem: (Int) -> Unit) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private val songList = mutableListOf<Song>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SongItemBinding.bind(itemView)
        fun bindData(data: Song, position: Int) {
            binding.textName.text = data.name
            binding.textInfor.text = context.resources.getString(
                R.string.text_music_infor,
                convertToDurationFormat(context, data.duration.toDouble()),
                data.singer
            )
            binding.root.setOnClickListener{onClickItem(position)}
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Song>) {
        songList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(songList[holder.adapterPosition], position)
    }

    override fun getItemCount(): Int {
        return songList.size
    }
}