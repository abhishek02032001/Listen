package com.example.listen.view.activity

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.listen.R
import com.example.listen.adapters.TabViewAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        setTheme(R.style.Theme_Listen)
        setContentView(R.layout.activity_main)

        val adapter = TabViewAdapter(supportFragmentManager, lifecycle)
        tabContainer.adapter = adapter

        setTabBar()
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 13){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permisssion Granted", Toast.LENGTH_LONG).show()

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 13)
        }
    }

    private fun setTabBar() {
        TabLayoutMediator(tabBar, tabContainer) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "SONGS"
                }
                1 -> {
                    tab.text = "ALBUMS"
                }
                2 -> {
                    tab.text = "PLAYLISTS"
                }
                3 -> {
                    tab.text = "Favourites"
                }
            }
        }.attach()
    }
}