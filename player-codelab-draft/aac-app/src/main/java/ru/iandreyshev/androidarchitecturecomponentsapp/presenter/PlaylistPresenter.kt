package ru.iandreyshev.androidarchitecturecomponentsapp.presenter

import ru.iandreyshev.model.playlist.IPlaylistPresenter
import ru.iandreyshev.model.playlist.Playlist

class PlaylistPresenter(
    private val playlist: Playlist
) {
    fun subscribe(presenter: IPlaylistPresenter) = playlist.subscribe(presenter)
    fun unsubscribe() = playlist.unsubscribe()
}
