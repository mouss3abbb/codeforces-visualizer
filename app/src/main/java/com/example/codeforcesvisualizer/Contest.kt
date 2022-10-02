package com.example.codeforcesvisualizer

data class Contest(
    val contestId: Int,
    val contestName: String,
    val handle: String,
    val newRating: Int,
    val oldRating: Int,
    val rank: Int,
    val ratingUpdateTimeSeconds: Int
)