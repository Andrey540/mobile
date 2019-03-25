package ru.iandreyshev.model.playlist

interface ITrack {
    val title: String
    val durationInMillis: Long

    fun play()
}
