package com.example.listen.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.listen.view.fragments.*

class TabViewAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                Songs()
            }
            1 -> {
                Albums()
            }
            2 -> {
                Playlist()
            }
            3 -> {
                Favourites()
            }
            else -> {
                Songs()
            }
        }
    }

}