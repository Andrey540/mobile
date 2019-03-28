package ru.iandreyshev.androidarchitecturecomponentsapp.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.iandreyshev.androidarchitecturecomponentsapp.presenter.PlayerPresenter
import ru.iandreyshev.androidarchitecturecomponentsapp.presenter.PlaylistPresenter
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.model.player.Timeline
import ru.iandreyshev.model.playlist.ITrack
import ru.iandreyshev.utils.toHumanReadableTime

class PlaylistViewModel(
    private val playerPresenter: PlayerPresenter,
    private val playlistPresenter: PlaylistPresenter
) : ViewModel(), IPlayerViewModel, IPlaylistViewModel {

    val trackTitle = MutableLiveData<String>()
    val trackPosterUrl = MutableLiveData<String>()
    val trackTimeline = MutableLiveData<TimelineViewModel>()
    val playingState = MutableLiveData<PlayingState>()
    val playList = MutableLiveData<List<ITrack>>()

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

    override fun updatePlaylist(playlist: List<ITrack>) {
        playList.value = playlist
    }

    override fun onCleared() {
        playerPresenter.unsubscribe(this)
        playlistPresenter.unsubscribe(this)
    }
}