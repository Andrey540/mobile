package ru.iandreyshev.androidarchitecturecomponentsapp.view

import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.model.player.Timeline

interface IPlayerViewModel {
    fun updateTitle(title: String?)
    fun updateTimeline(timeline: Timeline)
    fun updatePlaying(state: PlayingState)
    fun updatePoster(url: String?)
}
