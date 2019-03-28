package ru.iandreyshev.androidarchitecturecomponentsapp.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.iandreyshev.androidarchitecturecomponentsapp.presenter.PlayerPresenter
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.model.player.Timeline
import ru.iandreyshev.utils.toHumanReadableTime

class PlayerViewModel(
    private val presenter: PlayerPresenter
) : ViewModel(), IPlayerViewModel {

    val trackTitle = MutableLiveData<String>()
    val trackPosterUrl = MutableLiveData<String>()
    val trackTimeline = MutableLiveData<TimelineViewModel>()
    val playingState = MutableLiveData<PlayingState>()

    override fun updateTitle(title: String?) {
        trackTitle.value = title
    }

    override fun updatePoster(url: String?) {
        trackPosterUrl.value = url
    }

    override fun updateTimeline(timeline: Timeline) {
        trackTimeline.value = TimelineViewModel(timeline.timeInMillis.toHumanReadableTime(), timeline.percent)
    }

    override fun updatePlaying(state: PlayingState) {
        playingState.value = state
    }

    override fun onCleared() {
        presenter.unsubscribe(this)
    }
}