package com.example.listen.services


import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.listen.R
import com.example.listen.view.activity.SongPlayer

class MusicService : Service(){
    var myBinder = MyBinder()
    var mediaPlayer : MediaPlayer? = null
    private lateinit var mediaSession : MediaSessionCompat

    override fun onBind(p0: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder: Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(){
        val notification = NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID)
            .setContentTitle(SongPlayer.musicList[SongPlayer.position].title)
            .setContentText(SongPlayer.musicList[SongPlayer.position].artist)
            .setSmallIcon(R.drawable.splash_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.splash_logo))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(false)
            .addAction(R.drawable.ic_previous, "Previous", null)
            .addAction(R.drawable.ic_play, "Play", null)
            .addAction(R.drawable.ic_forward, "Next", null)
            .build()

        startForeground(13, notification)
    }

}