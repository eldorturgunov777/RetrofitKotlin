package com.example.retrofitkotlin.model

data class Note(
    val title: String,
    val body: String,
    var id: Int,
    val userId: Int
)