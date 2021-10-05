package com.example.listen.models

data class SongModel(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long,
    val path: String,
    val imagePath: String
)
