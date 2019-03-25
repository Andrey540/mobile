package ru.iandreyshev.model.playlist

class Track(
    override val title: String,
    override val durationInMillis: Long,
    private val onPlay: () -> Unit
) : ITrack {

    override fun play() = onPlay()

}
