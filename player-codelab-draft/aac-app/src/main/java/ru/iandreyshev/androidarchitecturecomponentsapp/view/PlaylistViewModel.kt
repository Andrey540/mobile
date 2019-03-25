package ru.iandreyshev.androidarchitecturecomponentsapp.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.iandreyshev.model.player.IPlayerPresenter
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.model.player.Timeline
import ru.iandreyshev.model.playlist.IPlaylistPresenter
import ru.iandreyshev.model.playlist.ITrack

class PlaylistViewModel : ViewModel(), IPlayerPresenter, IPlaylistPresenter {

    val trackTitle = MutableLiveData<String>()
    val trackPosterUrl = MutableLiveData<String>()
    val trackTimeline = MutableLiveData<Timeline>()
    val playingState = MutableLiveData<PlayingState>()
    val playList = MutableLiveData<List<ITrack>>()

    override fun updateTitle(title: String?) {
        trackTitle.value = title
    }

    override fun updatePoster(url: String?) {
        trackPosterUrl.value = url
    }

    override fun updateTimeline(timeline: Timeline) {
        trackTimeline.value = timeline
    }

    override fun updatePlaying(state: PlayingState) {
        playingState.value = state
    }

    override fun updatePlaylist(playlist: List<ITrack>) {
        playList.value = playlist
    }
}