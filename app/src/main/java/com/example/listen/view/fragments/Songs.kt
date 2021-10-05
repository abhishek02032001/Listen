package com.example.listen.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listen.R
import com.example.listen.adapters.ClickHandler
import com.example.listen.adapters.SongListAdapter
import com.example.listen.models.SongModel
import com.example.listen.view.activity.SongPlayer
import kotlinx.android.synthetic.main.fragment_songs.view.*

class Songs : Fragment(), ClickHandler {

    companion object{
        lateinit var musicList:ArrayList<SongModel>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_songs, container, false)
        musicList = getAllAudio()
        view.songListView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = SongListAdapter(musicList, this, requireActivity())
        view.songListView.adapter = adapter
        return view
    }

    private fun getAllAudio(): ArrayList<SongModel> {
        val songList: ArrayList<SongModel> = ArrayList()
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor = requireActivity().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC",
            null
        )
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                    val id = cursor.getString(idIndex)
                    val titleIndex:Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val title = cursor.getString(titleIndex)
                    val albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                    val album = cursor.getString(albumIndex)
                    val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    val artist = cursor.getString(artistIndex)
                    val durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    val duration = cursor.getLong(durationIndex)
                    val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    val albumId = cursor.getLong(albumIdIndex).toString()
                    val uri  = Uri.parse("content://media/external/audio/albumart")
                    val imagePath = Uri.withAppendedPath(uri, albumId).toString()
                    val pathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                    val path = cursor.getString(pathIndex)
                    val songModelObject = SongModel(id, title, album, artist, duration, path, imagePath)
                    songList.add(songModelObject)
                }while(cursor.moveToNext())
                cursor.close()
            }
        }

        return songList
    }

    override fun onSongClick(position: Int) {
        var intent = Intent(requireActivity(), SongPlayer::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    fun getStringDuration(millis : Long):String{
        var res : String = ""
        var minutes = (millis/1000)/60
        var seconds = (millis/1000)%60
        res = if(minutes in 0..9){
            if(seconds in 0..9){
                "0$minutes:0$seconds"
            } else{
                "0$minutes:$seconds"
            }
        }else{
            if(seconds in 0..9){
                "$minutes:0$seconds"
            } else{
                "$minutes:$seconds"
            }
        }
        return res
    }
}