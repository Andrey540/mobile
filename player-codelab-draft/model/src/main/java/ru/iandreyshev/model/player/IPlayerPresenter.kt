package ru.iandreyshev.model.player

interface IPlayerPresenter {
    fun updateTitle(title: String?)
    fun updateTimeline(timeline: Timeline)
    fun updatePlaying(state: PlayingState)
    fun updatePoster(url: String?)
}
