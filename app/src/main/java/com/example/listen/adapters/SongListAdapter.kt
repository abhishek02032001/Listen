package com.example.listen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.listen.R
import com.example.listen.models.SongModel
import kotlinx.android.synthetic.main.song_view.view.*

class SongListAdapter(var list : ArrayList<SongModel>, var listener:ClickHandler, var context:Context) : RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {

    var songList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_view, parent, false)
        return SongListViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val currentSong = songList[position]
        holder.songTitle.text = currentSong.title
        holder.songDuration.text = currentSong.duration.toString()
        holder.songArtist.text = currentSong.artist
        Glide.with(context)
            .load(songList[position].imagePath)
            .apply(RequestOptions.placeholderOf(R.drawable.splash_logo))
            .into(holder.songImage)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    inner class SongListViewHolder(itemView: View, listener: ClickHandler) : RecyclerView.ViewHolder(itemView) {
        val songImage: ImageView = itemView.songImage
        val songTitle: TextView = itemView.songTitle
        val songDuration: TextView = itemView.songDuration
        val songArtist: TextView = itemView.songArtist
        init {
            itemView.setOnClickListener{
                listener.onSongClick(adapterPosition)
            }
        }
    }

}

interface ClickHandler{
    fun onSongClick(position: Int)
}