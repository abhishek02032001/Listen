package com.example.listen.view.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.listen.R
import com.example.listen.models.SongModel
import com.example.listen.services.MusicService
import com.example.listen.view.fragments.Songs
import kotlinx.android.synthetic.main.activity_song_player.*
//import androidx.appcompat.widget.*

class SongPlayer : AppCompatActivity(), ServiceConnection {

    companion object {
        var isPlaying: Boolean = false
        var musicList: ArrayList<SongModel> = ArrayList()
        var position: Int = 0
        var isNext: Boolean = false
        var musicService: MusicService? = null
    }

    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_player)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)

        initializeLayout()

        playPauseBtn.setOnClickListener {
            if (isPlaying) pauseSong()
            else playSong()
        }

        nextBtn.setOnClickListener {
            isNext = true
            prevNext()
        }

        prevBtn.setOnClickListener {
            isNext = false
            prevNext()
        }

        playerController.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
               if(p2) musicService!!.mediaPlayer!!.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })

    }

    private fun initializeLayout() {
        musicList = Songs.musicList
        position = intent.getIntExtra("position", 0)
    }

    private fun setMusicPlayer() {
        songName.text = musicList[position].title
        songArtist.text = musicList[position].artist
        totalTime.text = getStringDuration(musicList[position].duration)
        Glide.with(this)
            .load(musicList[position].imagePath)
            .apply(RequestOptions.placeholderOf(R.drawable.splash_logo))
            .into(songImage)

        if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
        musicService!!.mediaPlayer!!.reset()
        musicService!!.mediaPlayer!!.setDataSource(musicList[position].path)
        musicService!!.mediaPlayer!!.prepare()
        musicService!!.mediaPlayer!!.start()
        currentTime.text = getStringDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
        playerController.progress = 0
        playerController.max = musicService!!.mediaPlayer!!.duration
        isPlaying = true
    }

    private fun playSong() {
        isPlaying = true
        playPauseBtn.setImageResource(R.drawable.ic_pause)
        musicService!!.mediaPlayer!!.start()
    }

    private fun pauseSong() {
        isPlaying = false
        playPauseBtn.setImageResource(R.drawable.ic_play)
        musicService!!.mediaPlayer!!.pause()
    }

    private fun prevNext() {
        if (isNext) {
            setPosition()
            setMusicPlayer()
        } else {
            setPosition()
            setMusicPlayer()
        }
    }

    private fun setPosition() {
        if (isNext) {
            if (position == (musicList.size - 1)) {
                position = 0
            } else {
                ++position
            }
        } else {
            if (position == 0) {
                position = musicList.size - 1
            } else {
                --position
            }
        }
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

    private fun seekBarSetUp(){
         runnable = Runnable {
             currentTime.text = getStringDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
             playerController.progress = musicService!!.mediaPlayer!!.currentPosition
             Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
         }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        var binder = p1 as MusicService.MyBinder
        musicService = binder.currentService()
        setMusicPlayer()
        musicService!!.showNotification()
        seekBarSetUp()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }
}